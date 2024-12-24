package ru.fabit.udf.example.presentation.store.state

/**
 * Используется в StatePayload для обработки изменений состояния. Используем аналогичто эвентам
 */
sealed interface MainChange {
    data class Text(val text: String) : MainChange
}