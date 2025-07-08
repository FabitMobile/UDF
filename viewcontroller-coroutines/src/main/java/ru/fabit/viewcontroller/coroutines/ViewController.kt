package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.Store
import ru.fabit.viewcontroller.core.LifecycleViewController
import ru.fabit.viewcontroller.core.StatePayload

abstract class ViewController<State, Action>(
    private val store: Store<State, Action>,
    statePayload: StatePayload<State>? = null
) : LifecycleViewController<State, Action>(statePayload) {

    init {
        store.start()
    }

    private var stateSubscription: Job? = null

    override fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun collectState() {
        stateSubscription = viewModelScope.launch {
            store.state.collect(::onNewState)
        }
    }

    override fun onPause(lifecycleOwner: LifecycleOwner) {
        super.onPause(lifecycleOwner)
        stateSubscription?.cancel()
    }

    override fun onCleared() {
        store.dispose()
    }
}