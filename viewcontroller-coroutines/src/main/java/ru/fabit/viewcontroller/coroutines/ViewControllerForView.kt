package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.BaseStore
import ru.fabit.viewcontroller.core.StatePayload
import ru.fabit.viewcontroller.core.ViewLifecycleViewController

abstract class ViewControllerForView<State, Action>(
    private val store: BaseStore<State, Action>,
    statePayload: StatePayload<State>? = null
) : ViewLifecycleViewController<State, Action>(statePayload) {

    init {
        store.start()
    }

    protected open fun detach() {}

    private var stateSubscription: Job? = null

    override fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun collectState(lifecycleOwner: LifecycleOwner) {
        stateSubscription = lifecycleOwner.lifecycleScope.launch {
            store.state.collect(::onNewState)
        }
    }

    override fun onResume(lifecycleOwner: LifecycleOwner) {
        attach()
    }

    override fun onStop(lifecycleOwner: LifecycleOwner) {
        super.onStop(lifecycleOwner)
        stateSubscription?.cancel()
        detach()
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        isAttached = false
        stateSubscription?.cancel()
        destroy(lifecycleOwner)
    }

    override fun dispose() {
        store.dispose()
    }
}