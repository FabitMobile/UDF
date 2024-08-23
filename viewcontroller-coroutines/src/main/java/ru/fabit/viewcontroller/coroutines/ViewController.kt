package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.Store
import ru.fabit.viewcontroller.core.StatePayload
import ru.fabit.viewcontroller.core.StateView
import ru.fabit.viewcontroller.coroutines.internal.log
import java.util.concurrent.atomic.AtomicReference

abstract class ViewController<State, Action>(
    private val store: Store<State, Action>,
    protected val statePayload: StatePayload<State>? = null
) : ViewModel(), LifecycleEventObserver {

    init {
        store.start()
    }

    protected open fun attach() {}

    protected open fun firstViewAttach() {}

    protected var view: StateView<State>? = null
    protected var isFirstAttach = true
    protected var isAttach = false

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

    protected open fun onCreate(lifecycleOwner: LifecycleOwner) {
        sharedState.set(null)
    }

    protected open fun onStart(lifecycleOwner: LifecycleOwner) {}

    @Suppress("UNCHECKED_CAST")
    protected open fun onResume(source: LifecycleOwner) {
        isAttach = true
        view = source as StateView<State>
        stateSubscription = viewModelScope.launch {
            store.state.collect { state ->
                val prevState = sharedState.get()
                sharedState.set(state)
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

    protected open fun onPause(source: LifecycleOwner) {
        isAttach = false
        stateSubscription?.cancel()
    }

    protected open fun onStop(lifecycleOwner: LifecycleOwner) {}

    protected open fun onDestroy(lifecycleOwner: LifecycleOwner) {
        store.dispose()
    }

    override fun onCleared() {
        store.dispose()
    }
}