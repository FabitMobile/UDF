package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

open class BaseStore<State, Action>(
    protected val storeKit: StoreKit<State, Action>
) : Store<State, Action> {

    constructor(
        startState: State,
        reducer: Reducer<State, Action>,
        errorHandler: ErrorHandler,
        bootstrapAction: Action? = null,
        sideEffects: Iterable<SideEffect<State, Action>> = emptyList(),
        bindActionSources: Iterable<BindActionSource<State, Action>> = emptyList(),
        actionSources: Iterable<ActionSource<Action>> = emptyList(),
        actionHandlers: Iterable<ActionHandler<State, Action>> = emptyList()
    ) : this(
        StoreKit<State, Action>(
            startState,
            bootstrapAction,
            reducer,
            errorHandler,
            sideEffects,
            bindActionSources,
            actionSources,
            actionHandlers
        )
    )

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val sideEffectsJobs = mutableMapOf<String, Job?>()
    private val actionHandlersJobs = mutableMapOf<String, Job?>()
    private val bindActionSourcesJobs = mutableMapOf<String, Job?>()
    private val actionSourcesJobs = mutableMapOf<String, Job?>()
    private var awaitSubscriptionJob: Job? = null

    protected val _actions = MutableSharedFlow<Action>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    protected val actions = _actions.asSharedFlow()

    protected val _state = MutableSharedFlow<State>(replay = 1)
    override val state: Flow<State>
        get() {
            _state.tryEmit(currentState)
            return _state
        }

    protected var _currentState: State = storeKit.startState
    override val currentState: State
        get() = _currentState

    override fun start() {
        awaitSubscription()
        scope.launch {
            handleActions()
        }
    }

    override fun dispatchAction(action: Action) {
        scope.launch {
            _actions.emit(action)
        }
    }

    override fun dispose() {
        scope.cancel()
        sideEffectsJobs.clear()
        actionHandlersJobs.clear()
        bindActionSourcesJobs.clear()
        actionSourcesJobs.clear()
        awaitSubscriptionJob?.cancel()
    }

    private fun awaitSubscription() {
        awaitSubscriptionJob = scope.launch {
            _actions.subscriptionCount
                .filter { it > 0 }
                .take(1)
                .collect { count ->
                    if (count > 0) {
                        dispatchActionSource()
                    }
                }
        }
    }

    protected open suspend fun handleActions() {
        actions.onSubscription {
            storeKit.bootstrapAction?.let {
                emit(it)
            }
        }.collect { action ->
            val state = reduceState(currentState, action)
            _state.emit(state)
            _currentState = state
            dispatchSideEffect(state, action)
            dispatchActionHandler(state, action)
            dispatchBindActionSource(state, action)
        }
    }

    protected open suspend fun reduceState(state: State, action: Action): State {
        return storeKit.reducer.reduceState(state, action)
    }

    protected open fun dispatchSideEffect(state: State, action: Action) {
        storeKit.actors.sideEffects.filter { sideEffect ->
            sideEffect.query(state, action)
        }.forEach { sideEffect ->
            sideEffectsJobs.start(sideEffect::class.java.simpleName) {
                scope.launch {
                    try {
                        _actions.emit(sideEffect(state, action))
                    } catch (t: Throwable) {
                        t.handleCancellationException {
                            storeKit.errorHandler.handle(t)
                            _actions.emit(sideEffect(t))
                        }
                    }
                }
            }
        }
    }

    protected open fun dispatchActionSource() {
        storeKit.actors.actionSources.map { actionSource ->
            actionSourcesJobs.start(actionSource::class.java.simpleName) {
                scope.launch {
                    try {
                        actionSource(this).catch {
                            it.handleCancellationException {
                                storeKit.errorHandler.handle(it)
                                emit(actionSource(it))
                            }
                        }.collect {
                            _actions.emit(it)
                        }
                    } catch (t: Throwable) {
                        t.handleCancellationException {
                            storeKit.errorHandler.handle(t)
                            _actions.emit(actionSource(t))
                        }
                    }
                }
            }
        }
    }

    protected open fun dispatchBindActionSource(state: State, action: Action) {
        storeKit.actors.bindActionSources.filter { bindActionSource ->
            bindActionSource.query(state, action)
        }.map { bindActionSource ->
            bindActionSourcesJobs.start(bindActionSource::class.java.simpleName) {
                scope.launch {
                    try {
                        bindActionSource(this, state, action).catch {
                            it.handleCancellationException {
                                storeKit.errorHandler.handle(it)
                                emit(bindActionSource(it))
                            }
                        }
                            .collect {
                                _actions.emit(it)
                            }
                    } catch (t: Throwable) {
                        t.handleCancellationException {
                            storeKit.errorHandler.handle(t)
                            _actions.emit(bindActionSource(t))
                        }
                    }
                }
            }
        }
    }

    protected open fun dispatchActionHandler(state: State, action: Action) {
        storeKit.actors.actionHandlers.filter { actionHandler ->
            actionHandler.query(state, action)
        }.forEach { actionHandler ->
            actionHandlersJobs.start(actionHandler::class.java.simpleName) {
                scope.launch(actionHandler.handlerDispatcher) {
                    try {
                        actionHandler(state, action)
                    } catch (t: Throwable) {
                        t.handleCancellationException { storeKit.errorHandler.handle(t) }
                    }
                }
            }
        }
    }

    private suspend inline fun Throwable.handleCancellationException(crossinline action: suspend () -> Unit) {
        if (this !is CancellationException) {
            action()
        }
    }

    private fun MutableMap<String, Job?>.start(key: String, action: () -> Job) {
        this[key]?.cancel()
        val job = action()
        this[key] = job
    }
}