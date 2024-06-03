package ru.fabit.viewcontroller.viewrxjava.payloadtest

import ru.fabit.viewcontroller.viewrxjava.EventsViewController
import ru.fabit.viewcontroller.viewrxjava.teststore.TestAction
import ru.fabit.viewcontroller.viewrxjava.teststore.TestEvent
import ru.fabit.viewcontroller.viewrxjava.teststore.TestState
import ru.fabit.viewcontroller.viewrxjava.teststore.TestStore

class PayloadViewController(store: TestStore, statePayload: TestStatePayload) :
    EventsViewController<TestState, TestAction, TestEvent>(store, statePayload) {
    fun event() {
        dispatchAction(TestAction.Event)
    }
}