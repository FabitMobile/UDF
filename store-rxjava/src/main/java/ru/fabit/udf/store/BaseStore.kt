package ru.fabit.udf.store

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.DisposableSubscriber

abstract class BaseStore<State : Any, Action : Any>(
    protected val storeKit: StoreKit<State, Action>
) : Store<State, Action> {

    constructor(
        currentState: State,
        bootstrapper: Action? = null,
        errorHandler: ErrorHandler,
        reducer: Reducer<State, Action>,
        sideEffects: Iterable<SideEffect<State, Action>> = emptyList(),
        actionSources: Iterable<ActionSource<Action>> = emptyList(),
        bindActionSources: Iterable<BindActionSource<State, Action>> = emptyList(),
        actionHandlers: Iterable<ActionHandler<State, Action>> = emptyList()
    ) : this(
        StoreKit<State, Action>(
            currentState,
            bootstrapper,
            reducer,
            errorHandler,
            sideEffects,
            bindActionSources,
            actionSources,
            actionHandlers
        )
    )

    protected val disposable = CompositeDisposable()
    protected val sourceDisposable = SourceDisposable(this::class.simpleName ?: "no_name")

    protected val actionSubject = PublishSubject.create<Action>()
    protected val stateSubject = BehaviorSubject.createDefault(storeKit.startState)

    override fun start() {
        disposable.add(handleActions())
        actionSourceDispatch()
        storeKit.bootstrapAction?.let { dispatchAction(it) }
    }

    override val currentState: State
        get() = stateSubject.value!!

    override val state: Observable<State> = stateSubject

    override fun dispatchAction(action: Action) {
        actionSubject.onNext(action)
    }

    override fun dispose() {
        disposable.clear()
        sourceDisposable.dispose()
    }

    protected open fun reduceState(state: State, action: Action): State {
        return storeKit.reducer.reduceState(state, action)
    }

    private fun handleActions(): Disposable {
        //Passage through the reducer should be one at a time
        val countRequestItem = 1L

        val subscriber = object : DisposableSubscriber<Action>() {
            override fun onStart() {
                request(countRequestItem)
            }

            override fun onComplete() {
            }

            override fun onNext(action: Action) {
                val state = reduceState(currentState, action)
                stateSubject.onNext(state)
                sideEffectDispatch(state, action)
                actionHandlerDispatch(state, action)
                bindActionSourceDispatch(state, action)
                request(countRequestItem)
            }

            override fun onError(t: Throwable?) {
                t?.let {
                    storeKit.errorHandler.handle(t)
                }
            }
        }
        actionSubject
            .toFlowable(BackpressureStrategy.BUFFER)
            .observeOn(Schedulers.computation())
            .subscribe(subscriber)
        return subscriber
    }

    private fun sideEffectDispatch(state: State, action: Action) {
        storeKit.actors.sideEffects.filter { sideEffect ->
            sideEffect.query(state, action)
        }.forEach { sideEffect ->
            val effect = try {
                sideEffect(state, action)
            } catch (t: Throwable) {
                Single.error(t)
            }
            sourceDisposable.add(
                sideEffect.key,
                effect
                    .doOnError { storeKit.errorHandler.handle(it) }
                    .onErrorResumeNext { throwable ->
                        Single.just(sideEffect(throwable))
                    }
                    .subscribe({ action ->
                        actionSubject.onNext(action)
                    }, {
                        storeKit.errorHandler.handle(it)
                    })
            )
        }
    }

    private fun actionHandlerDispatch(state: State, action: Action) {
        storeKit.actors.actionHandlers.filter { actionHandler ->
            actionHandler.query(state, action)
        }.forEach { actionHandler ->
            val handler = try {
                Single.create { it.onSuccess(actionHandler(state, action)) }
            } catch (t: Throwable) {
                Single.error(t)
            }
            sourceDisposable.add(
                actionHandler.key,
                handler
                    .doOnError { storeKit.errorHandler.handle(it) }
                    .subscribeOn(actionHandler.handlerScheduler)
                    .subscribe({ }, {
                        storeKit.errorHandler.handle(it)
                    })
            )
        }
    }

    private fun actionSourceDispatch() {
        storeKit.actors.actionSources.forEach { actionSource ->
            val source = try {
                actionSource()
            } catch (t: Throwable) {
                Observable.error(t)
            }
            sourceDisposable.add(
                actionSource.key,
                source
                    .doOnError { storeKit.errorHandler.handle(it) }
                    .onErrorResumeNext { throwable: Throwable ->
                        Observable.create { emitter ->
                            emitter.onNext(actionSource(throwable))
                        }
                    }
                    .subscribe({ action ->
                        actionSubject.onNext(action)
                    }, {
                        storeKit.errorHandler.handle(it)
                    })
            )
        }
    }

    private fun bindActionSourceDispatch(state: State, action: Action) {
        storeKit.actors.bindActionSources.filter { actionSource ->
            actionSource.query(state, action)
        }.forEach { actionSource ->
            val source = try {
                actionSource(state, action)
            } catch (t: Throwable) {
                Observable.error(t)
            }
            sourceDisposable.add(
                actionSource.key,
                source
                    .doOnError { storeKit.errorHandler.handle(it) }
                    .onErrorResumeNext { throwable: Throwable ->
                        Observable.create { emitter ->
                            emitter.onNext(actionSource(throwable))
                        }
                    }
                    .subscribe({ action ->
                        actionSubject.onNext(action)
                    }, {
                        storeKit.errorHandler.handle(it)
                    })
            )
        }
    }
}