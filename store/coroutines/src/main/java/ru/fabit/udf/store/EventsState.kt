package ru.fabit.udf.store

interface EventsState<Event> {
    val events: List<Event>

    fun clearEvents(): EventsState<Event>
}