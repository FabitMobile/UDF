package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

open class BindActionSource<State, Action>(
    val query: (State, Action) -> Boolean,
    private val source: suspend CoroutineScope.(State, Action) -> Flow<Action>,
    private val error: (Throwable) -> Action
) {
    suspend operator fun invoke(scope: CoroutineScope, state: State, action: Action) =
        source(scope, state, action)

    operator fun invoke(throwable: Throwable) = error(throwable)
}