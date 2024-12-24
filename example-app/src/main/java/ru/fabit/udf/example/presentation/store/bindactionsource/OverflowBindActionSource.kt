package ru.fabit.udf.example.presentation.store.bindactionsource

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.BindActionSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

/**
 * BindActionSource создает Flow, что позволяет создавать сразу несколько экшенов.
 * При выполнении условия query поток будет создан заново
 */
class OverflowBindActionSource @Inject constructor(
) : BindActionSource<MainState, MainAction>(
    query = { _, action ->
        action is MainAction.DataLoaded && action.counter > 4
    },
    source = { _, _ ->
        flow {
            emit(MainAction.Error(IllegalStateException("Too many requests")))
            emit(MainAction.SetButtonEnabled(false))
            delay(3.seconds)
            emit(MainAction.SetButtonEnabled(true))
        }
    },
    error = { MainAction.Error(it) }
)