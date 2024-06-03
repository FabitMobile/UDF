package ru.fabit.udf.store.order

import kotlinx.coroutines.flow.flow
import ru.fabit.udf.store.coroutines.ActionSource

class OrderActionSource(private val delay: Long) : ActionSource<OrderAction>(
    source = {
        flow {

            repeat(10) {
                kotlinx.coroutines.delay(delay)
                emit(OrderAction.Action(it.toString()))
            }
        }
    },
    error = {
        OrderAction.NoAction
    }
)