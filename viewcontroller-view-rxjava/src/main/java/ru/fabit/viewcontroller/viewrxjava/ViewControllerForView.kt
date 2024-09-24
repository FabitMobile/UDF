package ru.fabit.viewcontroller.viewrxjava

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.fabit.udf.store.BaseStore
import ru.fabit.viewcontroller.core.StatePayload
import ru.fabit.viewcontroller.core.StateView
import ru.fabit.viewcontroller.viewrxjava.internal.log
import java.util.concurrent.atomic.AtomicReference

abstract class ViewControllerForView<State : Any, Action : Any>(
    private val store: BaseStore<State, Action>,
    private val statePayload: StatePayload<State>? = null
) : ViewModel(), LifecycleEventObserver {

    init {
        store.start()
    }

    protected open fun attach() {}
    protected open fun firstViewAttach() {}
    protected open fun detach() {}

    private var createdView: LifecycleOwner? = null
    protected var resumedView: StateView<State>? = null
    protected var isAttached = false
    protected var isFirstAttach = false

    private val sharedState: AtomicReference<State> = AtomicReference()

    private var stateObserver: DisposableObserver<State>? = null

    protected fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        log("onStateChanged source=$source event=$event")
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate(source)
            Lifecycle.Event.ON_START -> onStart(source)
            Lifecycle.Event.ON_RESUME -> onResume(source)
            Lifecycle.Event.ON_PAUSE -> onPause(source)
            Lifecycle.Event.ON_STOP -> onStop(source)
            Lifecycle.Event.ON_DESTROY -> onDestroy(source)

            else -> {}
        }
    }

    protected open fun onCreate(lifecycleOwner: LifecycleOwner) {
        createdView = lifecycleOwner
        sharedState.set(null)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onStart(lifecycleOwner: LifecycleOwner) {
        resumedView = lifecycleOwner as StateView<State>
        isAttached = true
        stateObserver = object : DisposableObserver<State>() {

            override fun onComplete() {
            }

            override fun onNext(state: State) {
                val prevState = sharedState.get()
                if (isAttached && (prevState != state)) {
                    sharedState.set(state)
                    if (statePayload == null) {
                        resumedView?.renderState(state, null)
                    } else {
                        val payload = statePayload.payload(prevState, state)
                        resumedView?.renderState(state, payload)
                    }
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }

        store.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateObserver!!)

        attach()
        if (!isFirstAttach) {
            isFirstAttach = true
            firstViewAttach()
        }
    }

    protected open fun onResume(lifecycleOwner: LifecycleOwner) {
        attach()
    }

    protected open fun onPause(lifecycleOwner: LifecycleOwner) {}

    protected open fun onStop(lifecycleOwner: LifecycleOwner) {
        isAttached = false
        stateObserver?.dispose()
        detach()
    }

    protected open fun onDestroy(lifecycleOwner: LifecycleOwner) {
        isAttached = false
        stateObserver?.dispose()
        destroy(lifecycleOwner)
    }

    private fun destroy(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner == this.createdView) {
            store.dispose()
        }
    }
}