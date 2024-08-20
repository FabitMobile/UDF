package ru.fabit.viewcontroller.teststore

sealed interface TestEvent {
    object Event : TestEvent
    data class EventValue(val value: Int) : TestEvent
}