package ru.fabit.udf.store.teststore

sealed interface TestAction {
    object Init : TestAction

    data class Value(val value: Int) : TestAction

    data class Event(val value: Int) : TestAction
}