package ru.fabit.udf.store.coroutines.counter

import ru.fabit.udf.store.coroutines.BindActionSource
import ru.fabit.udf.store.coroutines.ErrorHandler
import ru.fabit.udf.store.coroutines.SideEffect
import ru.fabit.udf.store.coroutines.ActionHandler
import ru.fabit.udf.store.coroutines.ActionSource
import ru.fabit.udf.store.coroutines.BaseStore
import java.util.concurrent.CopyOnWriteArrayList

class CounterStore(
    currentState: CounterState,
    reducer: CounterReducer,
    errorHandler: ErrorHandler,
    bootStrapAction: CounterAction,
    actionHandlers: Iterable<ActionHandler<CounterState, CounterAction>> = CopyOnWriteArrayList(),
    actionSources: Iterable<ActionSource<CounterAction>> = CopyOnWriteArrayList(),
    bindActionSources: Iterable<BindActionSource<CounterState, CounterAction>> = CopyOnWriteArrayList(),
    sideEffects: Iterable<SideEffect<CounterState, CounterAction>> = CopyOnWriteArrayList()
) : BaseStore<CounterState, CounterAction>(
    currentState,
    reducer,
    errorHandler,
    bootStrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)