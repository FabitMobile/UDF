package ru.fabit.udf.example.presentation.store.actionhandler

import kotlinx.coroutines.Dispatchers
import ru.fabit.udf.example.domain.usecase.IncreaseCounterUseCase
import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.ActionHandler
import ru.fabit.udf.store.coroutines.actionIs
import javax.inject.Inject

/**
 * Нажатие кнопки не порождает состояние - используем ActionHandler
 * указываем handlerDispatcher т.к. предполагается работа не в основном потоке
 */
class ButtonClickActionHandler @Inject constructor(
    increaseCounterUseCase: IncreaseCounterUseCase
) : ActionHandler<MainState, MainAction>(
    query = actionIs<MainAction.ButtonClick>(),
    handler = { _, _ ->
        increaseCounterUseCase.build()
    },
    handlerDispatcher = Dispatchers.IO
)