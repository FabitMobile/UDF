package ru.fabit.udf.example.presentation.store.action

sealed interface MainAction {
    object BackClick : MainAction
    object ButtonClick : MainAction
    object FeatureToggled : MainAction
    data class SetFeatureEnabled(val isEnabled: Boolean) : MainAction
    data class SetButtonEnabled(val isEnabled: Boolean) : MainAction
    data class DataLoaded(val counter: Int) : MainAction
    data class Error(val throwable: Throwable) : MainAction
}