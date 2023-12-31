package ru.fabit.udf.store.order

import kotlinx.coroutines.flow.flowOf
import ru.fabit.udf.store.BindActionSource

class OrderBindActionSource : BindActionSource<OrderState, OrderAction>(
    query = { _, action -> action is OrderAction.Action },
    source = { _, _ ->
        flowOf(OrderAction.NoAction)
    },
    error = { OrderAction.NoAction }
)