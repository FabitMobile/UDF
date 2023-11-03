package ru.fabit.udf.store

import io.reactivex.Observable

open class ActionSource<Action>(
    private val source: () -> Observable<Action>,
    private val error: (Throwable) -> Action
) {
    open val key: String = this::class.qualifiedName ?: this::class.java.simpleName

    operator fun invoke() = source()

    operator fun invoke(throwable: Throwable): Action = error(throwable)
}