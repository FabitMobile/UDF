package ru.fabit.viewcontroller.viewrxjava.teststore

sealed interface TestEvent {
    object Event : TestEvent
    data class EventValue(val value: Int) : TestEvent
}