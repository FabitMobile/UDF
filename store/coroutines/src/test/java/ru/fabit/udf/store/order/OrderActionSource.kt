package ru.fabit.udf.store.order

import kotlinx.coroutines.flow.flow

class OrderActionSource(private val delay: Long) : ru.fabit.udf.store.ActionSource<OrderAction>(
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