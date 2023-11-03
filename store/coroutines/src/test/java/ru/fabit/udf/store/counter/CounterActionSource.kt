package ru.fabit.udf.store.counter

import kotlinx.coroutines.flow.flow
import ru.fabit.udf.store.ActionSource

class CounterActionSource(
    private val repeat: Int,
    private val delay: Long
) : ru.fabit.udf.store.ActionSource<CounterAction>(
    source = {
        flow {
            repeat(repeat) {
                kotlinx.coroutines.delay(delay)
                emit(CounterAction.Action(1))
            }
        }
    },
    error = {
        CounterAction.NoAction
    }
)