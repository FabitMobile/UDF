package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.Flow

interface Store<State, Action> {

    fun start()

    val state: Flow<State>

    val currentState: State

    fun dispatchAction(action: Action)

    fun dispose()
}