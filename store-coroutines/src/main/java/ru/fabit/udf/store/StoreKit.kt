package ru.fabit.udf.store

data class StoreKit<State, Action>(
    val startState: State,
    val bootstrapAction: Action? = null,
    val reducer: Reducer<State, Action>,
    val errorHandler: ErrorHandler,
    val actors: Actors<State, Action> = Actors()
) {
    constructor(
        startState: State,
        bootstrapAction: Action? = null,
        reducer: Reducer<State, Action>,
        errorHandler: ErrorHandler,
        sideEffects: Iterable<SideEffect<State, Action>> = emptyList(),
        bindActionSources: Iterable<BindActionSource<State, Action>> = emptyList(),
        actionSources: Iterable<ActionSource<Action>> = emptyList(),
        actionHandlers: Iterable<ActionHandler<State, Action>> = emptyList()
    ) : this(
        startState,
        bootstrapAction,
        reducer,
        errorHandler,
        Actors(
            sideEffects,
            bindActionSources,
            actionSources,
            actionHandlers
        )
    )

    companion object {
        @Suppress("UNCHECKED_CAST")
        inline fun <reified State, reified Action> build(vararg params: Any): StoreKit<State, Action> {
            var state: State? = null
            var bootstrapAction: Action? = null
            var reducer: Reducer<State, Action>? = null
            var errorHandler: ErrorHandler? = null
            var actors: Actors<State, Action>? = null
            val sideEffects = mutableListOf<SideEffect<State, Action>>()
            val bindActionSources = mutableListOf<BindActionSource<State, Action>>()
            val actionSources = mutableListOf<ActionSource<Action>>()
            val actionHandlers = mutableListOf<ActionHandler<State, Action>>()
            params.forEach {
                when (it) {
                    is State -> state = it
                    is Action -> bootstrapAction = it
                    is Reducer<*, *> -> reducer = it as Reducer<State, Action>
                    is ErrorHandler -> errorHandler = it
                    is Actors<*, *> -> actors = it as Actors<State, Action>
                    is SideEffect<*, *> -> sideEffects.add(it as SideEffect<State, Action>)
                    is BindActionSource<*, *> -> bindActionSources.add(it as BindActionSource<State, Action>)
                    is ActionSource<*> -> actionSources.add(it as ActionSource<Action>)
                    is ActionHandler<*, *> -> actionHandlers.add(it as ActionHandler<State, Action>)
                    is Iterable<*> -> {
                        when (it.firstOrNull()) {
                            is SideEffect<*, *> -> sideEffects.addAll(it as Iterable<SideEffect<State, Action>>)
                            is BindActionSource<*, *> -> bindActionSources.addAll(it as Iterable<BindActionSource<State, Action>>)
                            is ActionSource<*> -> actionSources.addAll(it as Iterable<ActionSource<Action>>)
                            is ActionHandler<*, *> -> actionHandlers.addAll(it as Iterable<ActionHandler<State, Action>>)
                        }
                    }
                }
            }
            return StoreKit(
                state!!,
                bootstrapAction,
                reducer!!,
                errorHandler!!,
                actors ?: Actors(
                    sideEffects.toList(),
                    bindActionSources.toList(),
                    actionSources.toList(),
                    actionHandlers.toList()
                )
            )
        }
    }
}

data class Actors<State, Action>(
    val sideEffects: Iterable<SideEffect<State, Action>> = emptyList(),
    val bindActionSources: Iterable<BindActionSource<State, Action>> = emptyList(),
    val actionSources: Iterable<ActionSource<Action>> = emptyList(),
    val actionHandlers: Iterable<ActionHandler<State, Action>> = emptyList()
)
