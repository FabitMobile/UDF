package ru.fabit.udf.store.order

import ru.fabit.udf.store.BindActionSource
import ru.fabit.udf.store.ErrorHandler
import ru.fabit.udf.store.SideEffect
import java.util.concurrent.CopyOnWriteArrayList

class OrderStore(
    currentState: OrderState,
    reducer: OrderReducer,
    errorHandler: ErrorHandler,
    bootStrapAction: OrderAction,
    actionHandlers: Iterable<ru.fabit.udf.store.ActionHandler<OrderState, OrderAction>> = CopyOnWriteArrayList(),
    actionSources: Iterable<ru.fabit.udf.store.ActionSource<OrderAction>> = CopyOnWriteArrayList(),
    bindActionSources: Iterable<BindActionSource<OrderState, OrderAction>> = CopyOnWriteArrayList(),
    sideEffects: Iterable<SideEffect<OrderState, OrderAction>> = CopyOnWriteArrayList()
) : ru.fabit.udf.store.BaseStore<OrderState, OrderAction>(
    currentState,
    reducer,
    errorHandler,
    bootStrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)