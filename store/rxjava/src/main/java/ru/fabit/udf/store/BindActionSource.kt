package ru.fabit.udf.store

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableSource

open class BindActionSource<State, Action>(
    val query: (State, Action) -> Boolean,
    private val source: (State, Action) -> Observable<Action>,
    private val error: (Throwable) -> Action
) {

    constructor(
        query: (State, Action) -> Boolean,
        source: Create<State, Action>,
        error: (Throwable) -> Action
    ) : this(query, { state, action ->
        Observable.create {
            source.emitter(it, state, action)
        }
    }, error)

    constructor(
        query: (State, Action) -> Boolean,
        source: Defer<State, Action>,
        error: (Throwable) -> Action
    ) : this(query, { state, action ->
        Observable.defer {
            source.observable.help(state, action)
        }
    }, error)

    open val key: String = this::class.qualifiedName ?: this::class.java.simpleName

    operator fun invoke(state: State, action: Action) = source(state, action)

    operator fun invoke(throwable: Throwable): Action = error(throwable)

    @JvmInline
    value class Create<State, Action>(val emitter: ObservableEmitter<Action>.(state: State, action: Action) -> Unit)

    @JvmInline
    value class Defer<State, Action>(val observable: DeferHelper<State, Action>)

    fun interface DeferHelper<State, Action> {
        fun help(state: State, acton: Action): ObservableSource<Action>
    }
}