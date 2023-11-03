package ru.fabit.udf.store

import kotlinx.coroutines.flow.Flow

open class BindActionSource<State, Action>(
    val query: (State, Action) -> Boolean,
    private val source: suspend (State, Action) -> Flow<Action>,
    private val error: (Throwable) -> Action
) {
    suspend operator fun invoke(state: State, action: Action) = source(state, action)

    operator fun invoke(throwable: Throwable) = error(throwable)
}