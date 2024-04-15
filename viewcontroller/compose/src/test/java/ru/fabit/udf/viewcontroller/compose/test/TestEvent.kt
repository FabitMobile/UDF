package ru.fabit.udf.viewcontroller.compose.test

sealed class TestEvent {
    class Event : TestEvent() {
        override fun toString(): String {
            return "TestEvent.Event"
        }
    }
    class Event2 : TestEvent() {
        override fun toString(): String {
            return "TestEvent.Event2"
        }
    }
}