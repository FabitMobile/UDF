package ru.fabit.udf.example.presentation.viewcontroller

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.fabit.udf.example.presentation.store.MainStore
import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainEvent
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.viewcontroller.coroutines.EventsViewController
import javax.inject.Inject

@HiltViewModel
class MainViewController @Inject constructor(
    store: MainStore
) : EventsViewController<MainState, MainAction, MainEvent>(store, MainPayload) {

    fun buttonClick() {
        dispatchAction(MainAction.ButtonClick)
    }

    fun featureToggled() {
        dispatchAction(MainAction.FeatureToggled)
    }

    fun backClick() {
        dispatchAction(MainAction.BackClick)
    }
}