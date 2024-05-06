package ru.fabit.udf.store

import io.reactivex.Single

open class SideEffect<State, Action>(
    val query: (State, Action) -> Boolean,
    private val effect: (State, Action) -> Single<Action>,
    private val error: (Throwable) -> Action
) {
    open val key: String = this::class.qualifiedName ?: this::class.java.simpleName

    operator fun invoke(state: State, action: Action) = effect(state, action)

    operator fun invoke(throwable: Throwable): Action = error(throwable)
}