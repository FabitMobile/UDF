package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.EventsStore
import ru.fabit.udf.store.coroutines.StateWithEvents
import ru.fabit.viewcontroller.core.EventsView
import ru.fabit.viewcontroller.core.StatePayload
import ru.fabit.viewcontroller.coroutines.internal.log
import java.util.concurrent.atomic.AtomicReference

abstract class EventsViewController<State : Any, Action : Any, Event : Any>(
    private val store: EventsStore<State, Action, Event>,
    statePayload: StatePayload<State>? = null
) : ViewController<State, Action>(store, statePayload) {

    private val sharedState: AtomicReference<StateWithEvents<State, Event>?> = AtomicReference()

    private var stateSubscription: Job? = null

    @Suppress("UNCHECKED_CAST")
    override fun onResume(lifecycleOwner: LifecycleOwner) {
        isAttached = true
        val resumedView = lifecycleOwner as? EventsView<State, Event>
        this.resumedView = resumedView
        stateSubscription = viewModelScope.launch {
            store.stateWithEvents.collect { stateWithEvents ->
                val state = stateWithEvents.state
                val events = stateWithEvents.events

                log("EventsViewController: state = $state, events = $events")
                val prevState = sharedState.get()
                sharedState.set(stateWithEvents)
                if (isAttached) {
                    val statePayload = statePayload
                    if (statePayload == null) {
                        resumedView?.renderState(state, events, null)
                    } else {
                        val payload = statePayload.payload(prevState?.state, state)
                        resumedView?.renderState(state, events, payload)
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

    override fun onPause(lifecycleOwner: LifecycleOwner) {
        super.onPause(lifecycleOwner)
        stateSubscription?.cancel()
    }
}