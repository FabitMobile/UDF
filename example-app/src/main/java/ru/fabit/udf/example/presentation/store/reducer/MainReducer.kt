package ru.fabit.udf.example.presentation.store.reducer

import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainEvent
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.EventsReducer

object MainReducer : EventsReducer<MainState, MainAction, MainEvent>() {
    override fun MainState.reduce(action: MainAction): MainState {
        return when (action) {
            is MainAction.ButtonClick -> copy(
                isLoading = true
            )

            is MainAction.DataLoaded -> copy(
                counter = action.counter,
                isLoading = false
            )

            is MainAction.SetFeatureEnabled -> copy(
                featureEnabled = action.isEnabled
            )

            is MainAction.SetButtonEnabled -> copy(
                buttonEnabled = action.isEnabled
            )

            is MainAction.Error -> copy() + MainEvent.Error(action.throwable.message ?: "error")

            is MainAction.BackClick -> copy() + MainEvent.ExitDialog

            else -> copy()
        }
    }
}