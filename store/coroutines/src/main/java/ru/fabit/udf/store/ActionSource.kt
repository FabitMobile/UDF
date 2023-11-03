package ru.fabit.udf.store

import kotlinx.coroutines.flow.Flow

open class ActionSource<Action>(
    private val source: suspend () -> Flow<Action>,
    private val error: (Throwable) -> Action = { t: Throwable -> throw t }
) {
    suspend operator fun invoke() = source()

    operator fun invoke(throwable: Throwable) = error(throwable)
}