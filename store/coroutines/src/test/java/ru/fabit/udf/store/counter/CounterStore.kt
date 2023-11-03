package ru.fabit.udf.store.counter

import ru.fabit.udf.store.*
import java.util.concurrent.CopyOnWriteArrayList

class CounterStore(
    currentState: CounterState,
    reducer: CounterReducer,
    errorHandler: ErrorHandler,
    bootStrapAction: CounterAction,
    actionHandlers: Iterable<ru.fabit.udf.store.ActionHandler<CounterState, CounterAction>> = CopyOnWriteArrayList(),
    actionSources: Iterable<ru.fabit.udf.store.ActionSource<CounterAction>> = CopyOnWriteArrayList(),
    bindActionSources: Iterable<BindActionSource<CounterState, CounterAction>> = CopyOnWriteArrayList(),
    sideEffects: Iterable<SideEffect<CounterState, CounterAction>> = CopyOnWriteArrayList()
) : ru.fabit.udf.store.BaseStore<CounterState, CounterAction>(
    currentState,
    reducer,
    errorHandler,
    bootStrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)