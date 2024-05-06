package ru.fabit.udf.store

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableSource
import java.util.concurrent.Callable

open class ActionSource<Action>(
    private val source: () -> Observable<Action>,
    private val error: (Throwable) -> Action
) {
    constructor(
        source: Create<Action>,
        error: (Throwable) -> Action
    ) : this({ Observable.create(source.emitter) }, error)

    constructor(
        source: Defer<Action>,
        error: (Throwable) -> Action
    ) : this({ Observable.defer(source.observable) }, error)

    open val key: String = this::class.qualifiedName ?: this::class.java.simpleName

    operator fun invoke() = source()

    operator fun invoke(throwable: Throwable): Action = error(throwable)

    @JvmInline
    value class Create<Action>(val emitter: ObservableEmitter<Action>.() -> Unit)

    @JvmInline
    value class Defer<Action>(val observable: Callable<ObservableSource<Action>>)
}