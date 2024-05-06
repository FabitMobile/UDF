package ru.fabit.viewcontroller.viewrxjava

import ru.fabit.viewcontroller.viewrxjava.teststore.TestAction
import ru.fabit.viewcontroller.viewrxjava.teststore.TestEvent
import ru.fabit.viewcontroller.viewrxjava.teststore.TestState
import ru.fabit.viewcontroller.viewrxjava.teststore.TestStore

class TestViewController(store: TestStore) :
    EventsViewController<TestState, TestAction, TestEvent>(store) {
    fun event() {
        dispatchAction(TestAction.Event)
    }
}