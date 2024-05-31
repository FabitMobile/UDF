package ru.fabit.viewcontroller.coroutines

interface StateView<State> {
    fun renderState(state: State)
}