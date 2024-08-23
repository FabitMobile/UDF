package ru.fabit.viewcontroller.core

fun interface StateView<State> {
    fun renderState(state: State, payload: Any?)
}