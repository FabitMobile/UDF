package ru.fabit.viewcontroller.viewrxjava

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ru.fabit.udf.store.BaseStore

abstract class BaseLifecycleAwareViewController<State : Any, Action : Any>(
    store: BaseStore<State, Action>,
    statePayload: StatePayload<State>? = null
) : ViewController<State, Action, StateView<State>>(store, statePayload),
    LifecycleObserver {

    protected open fun detach() {}

    override fun onStart(lifecycleOwner: LifecycleOwner) {
        super.onResume(lifecycleOwner)
    }

    override fun onResume(lifecycleOwner: LifecycleOwner) {
        attach()
    }

    override fun onPause(lifecycleOwner: LifecycleOwner) {}

    override fun onStop(lifecycleOwner: LifecycleOwner) {
        super.onPause(lifecycleOwner)
        detach()
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        super.onPause(lifecycleOwner)
        destroy(lifecycleOwner)
    }
}