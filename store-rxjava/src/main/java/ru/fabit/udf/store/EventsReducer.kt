package ru.fabit.udf.store

abstract class EventsReducer<State, Action, Event> : Reducer<State, Action> {

    private val events: MutableList<Event> = mutableListOf()

    fun reduceStateWithEvents(state: State, action: Action): StateWithEvents<State, Event> {
        val newState = reduceState(state, action)
        val newEvents = events.toList()
        events.clear()
        return StateWithEvents(newState, newEvents)
    }

    protected operator fun State.plus(event: Event): State {
        events.add(event)
        return this
    }

    protected operator fun State.plus(event: List<Event>): State {
        events.addAll(event)
        return this
    }
}

class StateWithEvents<State, Event>(val state: State, val events: List<Event>) {
    operator fun component1(): State = state

    operator fun component2(): List<Event> = events

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StateWithEvents<*, *>

        if (state != other.state) return false
        return events == other.events
    }

    override fun hashCode(): Int {
        var result = state?.hashCode() ?: 0
        result = 31 * result + events.hashCode()
        return result
    }
}