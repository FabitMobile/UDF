package ru.fabit.viewcontroller.coroutines

interface StatePayload<State> {
    fun payload(prevState: State?, newState: State): Any?
}