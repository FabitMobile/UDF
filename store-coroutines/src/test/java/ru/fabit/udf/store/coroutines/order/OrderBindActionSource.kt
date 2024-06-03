package ru.fabit.udf.store.coroutines.order

import kotlinx.coroutines.flow.flowOf
import ru.fabit.udf.store.coroutines.BindActionSource

class OrderBindActionSource : BindActionSource<OrderState, OrderAction>(
    query = { _, action -> action is OrderAction.Action },
    source = { _, _ ->
        flowOf(OrderAction.NoAction)
    },
    error = { OrderAction.NoAction }
)