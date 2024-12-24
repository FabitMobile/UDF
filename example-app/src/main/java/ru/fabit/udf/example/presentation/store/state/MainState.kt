package ru.fabit.udf.example.presentation.store.state

data class MainState(
    val isLoading: Boolean = true,
    val counter: Int? = null,
    val featureEnabled: Boolean? = null,
    val buttonEnabled: Boolean = true
)