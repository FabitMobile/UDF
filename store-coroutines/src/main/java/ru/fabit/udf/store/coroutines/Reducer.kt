package ru.fabit.udf.store.coroutines

fun interface Reducer<State, Action> {
    fun reduceState(state: State, action: Action): State {
        return state.reduce(action)
    }

    fun State.reduce(action: Action): State
}