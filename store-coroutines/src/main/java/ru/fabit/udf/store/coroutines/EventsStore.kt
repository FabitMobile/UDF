package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import ru.fabit.udf.store.coroutines.internal.log

open class EventsStore<State, Action, Event>(storeKit: StoreKit<State, Action>) :
    BaseStore<State, Action>(storeKit) {

    private val _eventsFlow = MutableSharedFlow<List<Event>>(replay = 1)

    private var accumulatedEvents = listOf<Event>()

    private var isEventsFlowConnected = false

    val eventsFlow: Flow<List<Event>> = _eventsFlow
        .onSubscription {
            _eventsFlow.emit(accumulatedEvents)
            isEventsFlowConnected = true
            accumulatedEvents = listOf()
        }
        .onCompletion {
            isEventsFlowConnected = false
        }

    override suspend fun reduceState(state: State, action: Action): State {
        return if (storeKit.reducer is EventsReducer<State, Action, *>) {
            val (newState, newEvents) = storeKit.reducer.reduceStateWithEvents(state, action)
            newEvents as List<Event>
            if (isEventsFlowConnected) {
                _eventsFlow.emit(newEvents)
            } else {
                val accumulatedEvents = accumulatedEvents + newEvents
                this.accumulatedEvents = accumulatedEvents
            }
            log("EventsStore: accumulatedEvents = $accumulatedEvents, newEvents = $newEvents, action = $action")
            newState
        } else
            super.reduceState(state, action)
    }
}