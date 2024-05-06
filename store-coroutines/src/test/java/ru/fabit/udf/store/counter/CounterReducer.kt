package ru.fabit.udf.store.counter

import ru.fabit.udf.store.Reducer

class CounterReducer : Reducer<CounterState, CounterAction> {
    override fun CounterState.reduce(action: CounterAction): CounterState {
        return when (action) {
            is CounterAction.BootstrapAction -> copy(
                value = value + action.value
            )

            is CounterAction.Action -> copy(
                value = value + action.value
            )

            is CounterAction.BindAction -> copy(
                value = value + action.value
            )

            else -> copy()
        }
    }
}