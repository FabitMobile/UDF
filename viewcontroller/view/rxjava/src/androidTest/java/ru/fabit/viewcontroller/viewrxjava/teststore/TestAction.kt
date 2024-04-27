package ru.fabit.viewcontroller.viewrxjava.teststore

sealed interface TestAction {
    object Init : TestAction

    data class Value(val value: Int) : TestAction

    object Event : TestAction

    data class EventValue(val value: Int) : TestAction
}