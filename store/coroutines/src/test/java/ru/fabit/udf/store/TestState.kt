package ru.fabit.udf.store

data class TestState(
    val value: String,
    override val events: List<TestEvent> = listOf()
) : EventsState<TestEvent> {

    override fun clearEvents(): EventsState<TestEvent> {
        return copy(events = listOf())
    }
}