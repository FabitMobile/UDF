package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.store.coroutines.EventsStore
import ru.fabit.udf.store.coroutines.StoreKit

class TestStore(storeKit: StoreKit<TestState, TestAction>) :
    EventsStore<TestState, TestAction, TestEvent>(storeKit)