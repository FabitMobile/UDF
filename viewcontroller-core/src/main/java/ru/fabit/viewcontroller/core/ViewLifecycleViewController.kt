package ru.fabit.viewcontroller.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.atomic.AtomicReference

abstract class ViewLifecycleViewController<State, Action>(
    protected val statePayload: StatePayload<State>? = null
) : LifecycleEventObserver {

    private val sharedState: AtomicReference<State> = AtomicReference()
    private var createdView: LifecycleOwner? = null
    protected var resumedView: StateView<State>? = null
    protected var isAttached = false
    protected var isFirstAttach = false

    protected open fun attach() {}
    protected open fun firstViewAttach() {}

    open fun dispatchAction(action: Action) {}

    protected open fun onNewState(state: State) {
        val prevState = sharedState.get()
        sharedState.set(state)
        if (isAttached) {
            if (statePayload == null) {
                resumedView?.renderState(state, null)
            } else {
                val payload = statePayload.payload(prevState, state)
                resumedView?.renderState(state, payload)
            }
        }
    }

    protected abstract fun collectState(lifecycleOwner: LifecycleOwner)
    protected abstract fun dispose()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
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
        isAttached = true
        resumedView = lifecycleOwner as? StateView<State>
        collectState(lifecycleOwner)
        attach()
        if (isFirstAttach) {
            isFirstAttach = false
            firstViewAttach()
        }
    }

    protected open fun onResume(lifecycleOwner: LifecycleOwner) {}

    protected open fun onPause(lifecycleOwner: LifecycleOwner) {}

    protected open fun onStop(lifecycleOwner: LifecycleOwner) {
        isAttached = false
    }

    protected open fun onDestroy(lifecycleOwner: LifecycleOwner) {}

    protected fun destroy(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner == this.createdView) {
            dispose()
        }
    }
}