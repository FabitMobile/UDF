package ru.fabit.viewcontroller.viewrxjava

import androidx.lifecycle.LifecycleOwner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.fabit.udf.store.Store
import ru.fabit.viewcontroller.core.LifecycleViewController
import ru.fabit.viewcontroller.core.StatePayload

abstract class ViewController<State : Any, Action : Any>(
    private val store: Store<State, Action>,
    statePayload: StatePayload<State>? = null
) : LifecycleViewController<State, Action>(statePayload) {

    init {
        store.start()
    }

    private var stateObserver: DisposableObserver<State>? = null

    override fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun collectState() {
        stateObserver = object : DisposableObserver<State>() {
            override fun onComplete() {}

            override fun onNext(state: State) {
                onNewState(state)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }

        store.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateObserver!!)
    }

    override fun onPause(lifecycleOwner: LifecycleOwner) {
        super.onPause(lifecycleOwner)
        stateObserver?.dispose()
    }

    override fun onCleared() {
        store.dispose()
    }
}