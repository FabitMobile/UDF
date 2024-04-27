package ru.fabit.udf.store

class TestStore(storeKit: StoreKit<TestState, TestAction>) :
    EventsStore<TestState, TestAction, TestEvent>(storeKit)