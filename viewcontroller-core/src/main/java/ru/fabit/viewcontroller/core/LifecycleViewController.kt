package ru.fabit.viewcontroller.core

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicReference

abstract class LifecycleViewController<State, Action>(
    protected open val statePayload: StatePayload<State>? = null
) : ViewModel(), LifecycleEventObserver {

    private val sharedState: AtomicReference<State> = AtomicReference()
    private var createdView: LifecycleOwner? = null
    protected var resumedView: StateView<State>? = null
    protected var isAttached = false
    protected var isFirstAttach = true

    protected open fun attach() {}
    protected open fun firstViewAttach() {}

    open fun dispatchAction(action: Action) {}

    protected open fun onNewState(state: State) {
        val prevState = sharedState.get()
        if (isAttached && (prevState != state)) {
            sharedState.set(state)
            val statePayload = statePayload
            if (statePayload == null) {
                resumedView?.renderState(state, null)
            } else {
                val payload = statePayload.payload(prevState, state)
                resumedView?.renderState(state, payload)
            }
        }
    }

    protected abstract fun collectState()

    protected open fun dispose() = onCleared()

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

    protected open fun onStart(lifecycleOwner: LifecycleOwner) {}

    @Suppress("UNCHECKED_CAST")
    protected open fun onResume(lifecycleOwner: LifecycleOwner) {
        resumedView = lifecycleOwner as? StateView<State>
        isAttached = true
        collectState()
        attach()
        if (isFirstAttach) {
            isFirstAttach = false
            firstViewAttach()
        }
    }

    protected open fun onPause(lifecycleOwner: LifecycleOwner) {
        isAttached = false
    }

    protected open fun onStop(lifecycleOwner: LifecycleOwner) {}

    protected open fun onDestroy(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.activity?.let { activity ->
                if (activity.isFinishing) {
                    destroy(lifecycleOwner)
                } else {
                    var anyParentIsRemoving = false
                    var parent = lifecycleOwner.parentFragment
                    while (!anyParentIsRemoving && parent != null) {
                        anyParentIsRemoving = parent.isRemoving
                        parent = parent.parentFragment
                    }
                    if (lifecycleOwner.isRemoving || anyParentIsRemoving) {
                        destroy(lifecycleOwner)
                    }
                }
            }
        }
        if (lifecycleOwner is Activity) {
            if (lifecycleOwner.isFinishing) {
                destroy(lifecycleOwner)
            }
        }
    }

    private fun destroy(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner == this.createdView) {
            dispose()
        }
    }
}