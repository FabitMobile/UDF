package ru.fabit.udf.store

abstract class EventsReducer<State, Action, Event> : Reducer<State, Action> {

    private val events: MutableList<Event> = mutableListOf()

    fun reduceEvent(state: State, action: Action): WrapState<State, Event> {
        val newState = reduceState(state, action)
        val eventsHelp = events.toList()
        events.clear()
        return WrapState(newState, eventsHelp)
    }

    operator fun State.plus(event: Event): State {
        events.add(event)
        return this
    }

    operator fun State.plus(event: List<Event>): State {
        events.addAll(event)
        return this
    }
}

class WrapState<State, Event>(val state: State, val events: List<Event>)