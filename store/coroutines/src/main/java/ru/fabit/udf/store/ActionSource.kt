package ru.fabit.udf.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

open class ActionSource<Action>(
    private val source: suspend CoroutineScope.() -> Flow<Action>,
    private val error: (Throwable) -> Action = { t: Throwable -> throw t }
) {
    suspend operator fun invoke(scope: CoroutineScope) = source(scope)

    operator fun invoke(throwable: Throwable) = error(throwable)
}