package ru.fabit.viewcontroller.teststore

import ru.fabit.udf.store.coroutines.EventsStore
import ru.fabit.udf.store.coroutines.StoreKit

class TestStore(storeKit: StoreKit<TestState, TestAction>) :
    EventsStore<TestState, TestAction, TestEvent>(storeKit)