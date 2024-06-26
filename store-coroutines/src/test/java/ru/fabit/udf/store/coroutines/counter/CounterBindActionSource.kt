package ru.fabit.udf.store.coroutines.counter

import kotlinx.coroutines.flow.flowOf
import ru.fabit.udf.store.coroutines.BindActionSource

class CounterBindActionSource : BindActionSource<CounterState, CounterAction>(
    query = { _, action -> action is CounterAction.Action },
    source = { _, _ ->
        flowOf(CounterAction.BindAction(1))
    },
    error = { CounterAction.NoAction }
)