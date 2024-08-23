package ru.fabit.viewcontroller.core

interface StatePayload<State> {
    fun payload(prevState: State?, newState: State): Any?
}