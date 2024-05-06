package ru.fabit.udf.store

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

open class ActionHandler<State, Action>(
    val query: (State, Action) -> Boolean,
    private val handler: (State, Action) -> Unit,
    val handlerScheduler: Scheduler = Schedulers.trampoline()
) {
    open val key: String = this::class.qualifiedName ?: this::class.java.simpleName

    operator fun invoke(state: State, action: Action) = handler(state, action)
}