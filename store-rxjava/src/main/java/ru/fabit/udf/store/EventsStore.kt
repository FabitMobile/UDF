package ru.fabit.udf.store

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.fabit.udf.store.internal.log

open class EventsStore<State : Any, Action : Any, Event : Any>(storeKit: StoreKit<State, Action>) :
    BaseStore<State, Action>(storeKit) {

    private val _stateWithEventsSubject =
        BehaviorSubject.createDefault<StateWithEvents<State, Event>>(
            StateWithEvents(
                storeKit.startState,
                listOf()
            )
        )

    private var accumulatedEvents = listOf<Event>()

    val stateWithEvents: Observable<StateWithEvents<State, Event>> =
        _stateWithEventsSubject.doOnEach {
            clearEvents()
        }

    private fun clearEvents() {
        log("EventsStore: eventsToClear = $accumulatedEvents")
        accumulatedEvents = listOf()
    }

    override fun reduceState(state: State, action: Action): State {
        return if (storeKit.reducer is EventsReducer<State, Action, *>) {
            val (newState, newEvents) = storeKit.reducer.reduceStateWithEvents(state, action)
            val accumulatedEvents = accumulatedEvents + newEvents as List<Event>

            log("EventsStore: accumulatedEvents = $accumulatedEvents")
            this.accumulatedEvents = accumulatedEvents
            _stateWithEventsSubject.onNext(StateWithEvents(newState, accumulatedEvents))
            newState
        } else
            super.reduceState(state, action)
    }
}