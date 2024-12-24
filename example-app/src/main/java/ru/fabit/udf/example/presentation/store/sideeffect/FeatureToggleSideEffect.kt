package ru.fabit.udf.example.presentation.store.sideeffect

import ru.fabit.udf.example.domain.usecase.SetFeatureEnabledUseCase
import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.SideEffect
import javax.inject.Inject

/**
 * Переключение возможно только после первичной загрузки состояния. Внутри effect можно быть
 * уверенным, что условия query выполнены
 */
class FeatureToggleSideEffect @Inject constructor(
    setFeatureEnabledUseCase: SetFeatureEnabledUseCase
) : SideEffect<MainState, MainAction>(
    query = { state, action ->
        state.featureEnabled != null && action is MainAction.FeatureToggled
    },
    effect = { state, _ ->
        val enabled = !state.featureEnabled!!
        setFeatureEnabledUseCase.build(enabled)
        MainAction.SetFeatureEnabled(enabled)
    },
    error = { MainAction.Error(it) }
)