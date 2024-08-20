package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.transform
import ru.fabit.udf.store.coroutines.internal.log

fun <T> Flow<T>.doOnEach(action: suspend (T) -> Unit): Flow<T> = transform { value ->
    return@transform emit(value).also {
        action(value)
    }
}

open class EventsStoreCopy<State, Action, Event>(storeKit: StoreKit<State, Action>) :
    BaseStore<State, Action>(storeKit) {

    private val _eventsFlow = MutableSharedFlow<List<Event>>(replay = 1)
    private val _stateWithEventsFlow = MutableSharedFlow<StateWithEvents<State, Event>>(replay = 1)

    private var accumulatedEvents = listOf<Event>()

    val eventsFlow: Flow<List<Event>> = _eventsFlow
//        .onSubscription { _eventsFlow.emit(accumulatedEvents) }
        .doOnEach { clearEvents() }

    val stateWithEvents: Flow<StateWithEvents<State, Event>> = _stateWithEventsFlow
        .doOnEach { clearEvents() }

    private fun clearEvents() {
        log("EventsStore: eventsToClear = $accumulatedEvents")
        accumulatedEvents = listOf()
    }

    override suspend fun reduceState(state: State, action: Action): State {
        return if (storeKit.reducer is EventsReducer<State, Action, *>) {
            val (newState, newEvents) = storeKit.reducer.reduceStateWithEvents(state, action)
            log("EventsStore: accumulatedEvents = $accumulatedEvents, newEvents = $newEvents, action = $action")
            val accumulatedEvents = accumulatedEvents + newEvents as List<Event>

            this.accumulatedEvents = accumulatedEvents
            _eventsFlow.emit(accumulatedEvents)
            _stateWithEventsFlow.emit(StateWithEvents(newState, accumulatedEvents))
            newState
        } else
            super.reduceState(state, action)
    }
}