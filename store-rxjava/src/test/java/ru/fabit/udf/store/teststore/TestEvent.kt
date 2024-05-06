package ru.fabit.udf.store.teststore

sealed interface TestEvent {
    object Event : TestEvent
}