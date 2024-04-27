package ru.fabit.udf.viewcontroller.compose.test

import androidx.compose.runtime.Immutable

sealed interface TestEvent {
    object BootstrapEvent : TestEvent

    @Immutable
    data class Event1(val a: String = "1") : TestEvent

    @Immutable
    data class Event2(val a: String = "2") : TestEvent

    @Immutable
    data class Event3(val a: String = "3") : TestEvent
}