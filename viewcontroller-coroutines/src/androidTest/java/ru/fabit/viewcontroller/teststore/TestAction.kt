package ru.fabit.viewcontroller.teststore

sealed interface TestAction {
    object Init : TestAction

    data class Value(val value: Int) : TestAction

    object Event : TestAction

    data class EventValue(val value: Int) : TestAction
}