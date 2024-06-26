package ru.fabit.udf.store.coroutines

sealed class TestEvent {
    object Event : TestEvent() {
        override fun toString(): String {
            return "TestEvent.Event"
        }
    }

    class Event2 : TestEvent() {
        override fun toString(): String {
            return "TestEvent.Event2"
        }
    }

    data class OrderEvent(val order: Int) : TestEvent()
}