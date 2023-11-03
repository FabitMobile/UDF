package ru.fabit.udf.store

sealed class TestEvent {
    object Event : TestEvent() {
        override fun toString(): String {
            return "TestEvent.Event"
        }
    }
    object Event2 : TestEvent() {
        override fun toString(): String {
            return "TestEvent.Event2"
        }
    }
    data class OrderEvent(val order: Int) : TestEvent()
}