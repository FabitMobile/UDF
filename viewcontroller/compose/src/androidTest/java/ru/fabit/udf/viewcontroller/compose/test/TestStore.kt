package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.store.EventsStore
import ru.fabit.udf.store.StoreKit

class TestStore(storeKit: StoreKit<TestState, TestAction>) :
    EventsStore<TestState, TestAction, TestEvent>(storeKit)