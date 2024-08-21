package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.BaseStore
import ru.fabit.viewcontroller.core.StatePayload
import ru.fabit.viewcontroller.core.StateView
import ru.fabit.viewcontroller.coroutines.internal.log
import java.util.concurrent.atomic.AtomicReference

abstract class ViewControllerForView<State, Action>(
    private val store: BaseStore<State, Action>,
    private val statePayload: StatePayload<State>? = null
) : LifecycleEventObserver {

    init {
        store.start()
    }

    protected open fun attach() {}
    protected open fun firstViewAttach() {}
    protected open fun detach() {}

    private var view: StateView<State>? = null
    private var isFirstAttach = true
    private var isAttach = false

    private val sharedState: AtomicReference<State> = AtomicReference()

    private var stateSubscription: Job? = null

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

    protected open fun onCreate(source: LifecycleOwner) {
        sharedState.set(null)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onStart(source: LifecycleOwner) {
        isAttach = true
        view = source as StateView<State>
        stateSubscription = source.lifecycleScope.launch {
            store.state.collect { state ->
                val prevState = sharedState.get()
                if (isAttach) {
                    if (statePayload == null) {
                        view?.renderState(state, null)
                    } else {
                        val payload = statePayload.payload(prevState, state)
                        view?.renderState(state, payload)
                    }
                }
            }
        }
        attach()
        if (isFirstAttach) {
            isFirstAttach = false
            firstViewAttach()
        }
    }

    protected open fun onResume(source: LifecycleOwner) {
        attach()
    }

    protected open fun onPause(source: LifecycleOwner) {}

    protected open fun onStop(source: LifecycleOwner) {
        isAttach = false
        stateSubscription?.cancel()
        detach()
    }

    protected open fun onDestroy(source: LifecycleOwner) {
        isAttach = false
        stateSubscription?.cancel()
        store.dispose()
    }
}