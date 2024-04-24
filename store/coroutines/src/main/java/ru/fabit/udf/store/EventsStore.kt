package ru.fabit.udf.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.fabit.udf.store.internal.log

open class EventsStore<State, Action, Event>(storeKit: StoreKit<State, Action>) :
    BaseStore<State, Action>(storeKit) {

    private val _eventsFlow = MutableSharedFlow<List<Event>>(replay = 1, extraBufferCapacity = 5)

    private var accumulatedEvents = listOf<Event>()

    val eventsFlow: Flow<List<Event>> = _eventsFlow

    /**
     * Необходимо вызвать при колучении эвентов
     */
    fun clearEvents() {
        log("EventsStore: eventsToClear = $accumulatedEvents")
        _eventsFlow.tryEmit(listOf())
        accumulatedEvents = listOf()
    }

    override suspend fun reduceState(state: State, action: Action): State {
        return if (storeKit.reducer is EventsReducer<State, Action, *>) {
            val (newState, newEvents) = storeKit.reducer.reduceStateWithEvents(state, action)
            val accumulatedEvents = accumulatedEvents + newEvents as List<Event>

            log("EventsStore: accumulatedEvents = $accumulatedEvents")
            this.accumulatedEvents = accumulatedEvents
            _eventsFlow.emit(accumulatedEvents)
            newState
        } else
            super.reduceState(state, action)
    }
}