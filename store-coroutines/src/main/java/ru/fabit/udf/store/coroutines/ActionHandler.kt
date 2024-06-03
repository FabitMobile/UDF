package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class ActionHandler<State, Action>(
    val query: (State, Action) -> Boolean,
    private val handler: suspend (State, Action) -> Unit,
    val handlerDispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    suspend operator fun invoke(state: State, action: Action) = handler(state, action)
}