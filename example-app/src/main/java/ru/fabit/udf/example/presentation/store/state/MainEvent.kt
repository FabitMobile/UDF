package ru.fabit.udf.example.presentation.store.state

/**
 * Эвенты бывают полезны для отправки во View единичного события. Самые распространенные
 * примеры - показ диалога, вывод ошибки
 */
sealed interface MainEvent {
    data class Error(val message: String) : MainEvent
    object ExitDialog : MainEvent
}