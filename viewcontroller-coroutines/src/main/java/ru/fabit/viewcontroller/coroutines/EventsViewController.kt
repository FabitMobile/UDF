package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.EventsStore
import ru.fabit.udf.store.coroutines.StateWithEvents
import java.util.concurrent.atomic.AtomicReference

abstract class EventsViewController<State : Any, Action : Any, Event : Any>(
    private val store: EventsStore<State, Action, Event>,
    statePayload: StatePayload<State>? = null
) : ViewController<State, Action>(store, statePayload) {

    private val sharedState: AtomicReference<StateWithEvents<State, Event>?> = AtomicReference()

    private var stateSubscription: Job? = null

    @Suppress("UNCHECKED_CAST")
    override fun onResume(source: LifecycleOwner) {
        isAttach = true
        val resumedView = source as? EventsView<State, Event>
        view = resumedView
        stateSubscription = viewModelScope.launch {
            store.stateWithEvents.collect { stateWithEvents ->
                val state = stateWithEvents.state
                val events = stateWithEvents.events

                val prevState = sharedState.get()
                if (isAttach && (prevState != state)) {
                    if (statePayload == null) {
                        resumedView?.renderState(state, null)
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