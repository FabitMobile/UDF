package ru.fabit.viewcontroller.viewrxjava

import androidx.lifecycle.LifecycleOwner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.fabit.udf.store.BaseStore
import ru.fabit.viewcontroller.core.StatePayload
import ru.fabit.viewcontroller.core.ViewLifecycleViewController

abstract class ViewControllerForView<State : Any, Action : Any>(
    private val store: BaseStore<State, Action>,
    statePayload: StatePayload<State>? = null
) : ViewLifecycleViewController<State, Action>(statePayload) {

    init {
        store.start()
    }

    protected open fun detach() {}

    private var stateObserver: DisposableObserver<State>? = null

    override fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun collectState(lifecycleOwner: LifecycleOwner) {
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

    override fun onResume(lifecycleOwner: LifecycleOwner) {
        attach()
    }

    override fun onStop(lifecycleOwner: LifecycleOwner) {
        super.onStop(lifecycleOwner)
        stateObserver?.dispose()
        detach()
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        isAttached = false
        stateObserver?.dispose()
        destroy(lifecycleOwner)
    }
}