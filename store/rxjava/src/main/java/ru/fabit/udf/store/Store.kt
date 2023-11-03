package ru.fabit.udf.store

import io.reactivex.Observable

interface Store<State, Action> {

    fun start()

    val state: Observable<State>

    val currentState: State

    fun dispatchAction(action: Action)

    fun dispose()
}