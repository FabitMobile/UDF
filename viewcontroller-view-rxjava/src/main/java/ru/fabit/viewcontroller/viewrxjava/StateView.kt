package ru.fabit.viewcontroller.viewrxjava

fun interface StateView<State> {
    fun renderState(state: State, payload: Any?)
}