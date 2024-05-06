package ru.fabit.udf.store

fun interface Reducer<State, Action> {
    fun reduceState(state: State, action: Action): State {
        return state.reduce(action)
    }

    fun State.reduce(action: Action): State
}