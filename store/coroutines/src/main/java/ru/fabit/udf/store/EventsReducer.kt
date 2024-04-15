package ru.fabit.udf.store

abstract class EventsReducer<State, Action, Event> : Reducer<State, Action> {

    private val events: MutableList<Event> = mutableListOf()

    fun reduceEvent(state: State, action: Action): WrapState<State, Event> {
//        println("___TEST___ EventsReducer state = $state action = $action events =$events")
        val newState = reduceState(state, action)
        val eventsHelp = events.toList()
        events.clear()
//        println("___TEST___ EventsReducer newState = $newState action = $action eventsHelp =$eventsHelp")
        return WrapState(newState, eventsHelp)
    }

    operator fun State.plus(event: Event): State {
        events.add(event)
//        println("___TEST___ Events plus events =$events")
        return this
    }

    operator fun State.plus(event: List<Event>): State {
        events.addAll(event)
        return this
    }
}

class WrapState<State, Event>(val state: State, val events: List<Event>)