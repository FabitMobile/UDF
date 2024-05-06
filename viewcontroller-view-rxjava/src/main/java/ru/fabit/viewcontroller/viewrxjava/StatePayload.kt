package ru.fabit.viewcontroller.viewrxjava

interface StatePayload<State> {

    fun payload(prevState: State?, newState: State): Any?
}