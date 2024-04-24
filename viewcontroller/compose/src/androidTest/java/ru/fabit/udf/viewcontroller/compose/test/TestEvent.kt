package ru.fabit.udf.viewcontroller.compose.test

sealed interface TestEvent {
    object BootstrapEvent : TestEvent

    object Event : TestEvent
}