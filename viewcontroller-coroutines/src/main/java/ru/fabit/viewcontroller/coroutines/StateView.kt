package ru.fabit.viewcontroller.coroutines

fun interface StateView<State> {
    fun renderState(state: State, payload: Any?)
}