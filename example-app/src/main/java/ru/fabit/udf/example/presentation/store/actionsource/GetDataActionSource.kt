package ru.fabit.udf.example.presentation.store.actionsource

import kotlinx.coroutines.flow.map
import ru.fabit.udf.example.domain.usecase.GetDataUseCase
import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.store.coroutines.ActionSource
import javax.inject.Inject

/**
 * Используем ActionSource для реактивного подхода получения данных
 */
class GetDataActionSource @Inject constructor(
    getDataUseCase: GetDataUseCase
) : ActionSource<MainAction>(
    source = {
        getDataUseCase.build().map { data ->
            MainAction.DataLoaded(data.counter)
        }
    },
    error = { MainAction.Error(it) }
)