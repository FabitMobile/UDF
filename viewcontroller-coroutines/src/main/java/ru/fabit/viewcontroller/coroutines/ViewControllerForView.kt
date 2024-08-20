package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ru.fabit.udf.store.coroutines.BaseStore

abstract class ViewControllerForView<State, Action>(
    store: BaseStore<State, Action>,
    statePayload: StatePayload<State>? = null
) : ViewController<State, Action>(store, statePayload),
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
        onCleared()
    }
}