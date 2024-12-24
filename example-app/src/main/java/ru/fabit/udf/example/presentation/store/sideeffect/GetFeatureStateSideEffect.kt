package ru.fabit.udf.example.presentation.store.sideeffect

import ru.fabit.udf.example.domain.usecase.IsFeatureEnabledUseCase
import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.SideEffect
import javax.inject.Inject

/**
 * Состояие фичи зависит от внешних условий. Получаем значение при инициализации экрана
 */
class GetFeatureStateSideEffect @Inject constructor(
    isFeatureEnabledUseCase: IsFeatureEnabledUseCase
) : SideEffect<MainState, MainAction>(
    query = { state, _ ->
        state.featureEnabled == null
    },
    effect = { _, _ ->
        val enabled = isFeatureEnabledUseCase.build()
        MainAction.SetFeatureEnabled(enabled)
    },
    error = { MainAction.Error(it) }
)