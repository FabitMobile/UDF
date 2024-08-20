package ru.fabit.viewcontroller.coroutines

fun interface EventsView<State, Event> : StateView<State> {
    fun renderState(state: State, events: List<Event>, payload: Any?)

    override fun renderState(state: State, payload: Any?) {
        renderState(state, listOf(), payload)
    }
}