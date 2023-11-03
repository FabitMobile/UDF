package ru.fabit.udf.store.order

import ru.fabit.udf.store.Reducer

class OrderReducer : Reducer<OrderState, OrderAction> {
    override fun OrderState.reduce(action: OrderAction): OrderState {
        return when (action) {
            is OrderAction.BootstrapAction -> copy(
                value = value + action.value
            )
            is OrderAction.Action -> copy(
                value = value + action.value
            )

            is OrderAction.BindAction -> copy(
                value = value + action.value
            )
            else -> copy()
        }
    }
}