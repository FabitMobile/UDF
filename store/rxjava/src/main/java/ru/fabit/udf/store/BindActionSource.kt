package ru.fabit.udf.store

import io.reactivex.Observable

open class BindActionSource<State, Action>(
    val query: (State, Action) -> Boolean,
    private val source: (State, Action) -> Observable<Action>,
    private val error: (Throwable) -> Action
) {
    open val key: String = this::class.qualifiedName ?: this::class.java.simpleName

    operator fun invoke(state: State, action: Action) = source(state, action)

    operator fun invoke(throwable: Throwable): Action = error(throwable)
}