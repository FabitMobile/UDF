package ru.fabit.udf.store.coroutines.order

import ru.fabit.udf.store.coroutines.BindActionSource
import ru.fabit.udf.store.coroutines.ErrorHandler
import ru.fabit.udf.store.coroutines.SideEffect
import ru.fabit.udf.store.coroutines.ActionHandler
import ru.fabit.udf.store.coroutines.ActionSource
import ru.fabit.udf.store.coroutines.BaseStore
import java.util.concurrent.CopyOnWriteArrayList

class OrderStore(
    currentState: OrderState,
    reducer: OrderReducer,
    errorHandler: ErrorHandler,
    bootStrapAction: OrderAction,
    actionHandlers: Iterable<ActionHandler<OrderState, OrderAction>> = CopyOnWriteArrayList(),
    actionSources: Iterable<ActionSource<OrderAction>> = CopyOnWriteArrayList(),
    bindActionSources: Iterable<BindActionSource<OrderState, OrderAction>> = CopyOnWriteArrayList(),
    sideEffects: Iterable<SideEffect<OrderState, OrderAction>> = CopyOnWriteArrayList()
) : BaseStore<OrderState, OrderAction>(
    currentState,
    reducer,
    errorHandler,
    bootStrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)