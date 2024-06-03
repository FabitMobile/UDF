package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

open class ActionSource<Action>(
    private val source: suspend CoroutineScope.() -> Flow<Action>,
    private val error: (Throwable) -> Action
) {
    suspend operator fun invoke(scope: CoroutineScope) = source(scope)

    operator fun invoke(throwable: Throwable) = error(throwable)
}