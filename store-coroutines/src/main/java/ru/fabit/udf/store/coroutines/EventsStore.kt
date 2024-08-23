package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import ru.fabit.udf.store.coroutines.internal.log

open class EventsStore<State, Action, Event>(storeKit: StoreKit<State, Action>) :
    BaseStore<State, Action>(storeKit) {

    private val _eventsFlow = MutableSharedFlow<List<Event>>(replay = 1)
    private val _stateWithEventsFlow = MutableSharedFlow<StateWithEvents<State, Event>>(replay = 1)

    private var accumulatedEvents = listOf<Event>()

    private var isEventsFlowConnected = false

    val eventsFlow: Flow<List<Event>>
        get() {
            _eventsFlow.tryEmit(accumulatedEvents)
            return _eventsFlow
                .onSubscription {
                    isEventsFlowConnected = true
                    accumulatedEvents = listOf()
                }
                .onCompletion {
                    isEventsFlowConnected = false
                }
        }

    val stateWithEvents: Flow<StateWithEvents<State, Event>>
        get() {
            _stateWithEventsFlow.tryEmit(StateWithEvents(currentState, accumulatedEvents))
            return _stateWithEventsFlow
                .onSubscription {
                    isEventsFlowConnected = true
                    accumulatedEvents = listOf()
                }
                .onCompletion {
                    isEventsFlowConnected = false
                }
        }

    override suspend fun reduceState(state: State, action: Action): State {
        return if (storeKit.reducer is EventsReducer<State, Action, *>) {
            val (newState, newEvents) = storeKit.reducer.reduceStateWithEvents(state, action)
            newEvents as List<Event>

            log("EventsStore: accumulatedEvents = $accumulatedEvents, newEvents = $newEvents, action = $action")
            if (isEventsFlowConnected) {
                _eventsFlow.emit(newEvents)
                _stateWithEventsFlow.emit(StateWithEvents(newState, newEvents))
            } else {
                val accumulatedEvents = accumulatedEvents + newEvents
                this.accumulatedEvents = accumulatedEvents
            }
            newState
        } else
            super.reduceState(state, action)
    }
}