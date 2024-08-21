package ru.fabit.viewcontroller.payloadtest

import ru.fabit.viewcontroller.coroutines.EventsViewController
import ru.fabit.viewcontroller.teststore.TestAction
import ru.fabit.viewcontroller.teststore.TestEvent
import ru.fabit.viewcontroller.teststore.TestState
import ru.fabit.viewcontroller.teststore.TestStore

class PayloadViewController(store: TestStore, statePayload: TestStatePayload) :
    EventsViewController<TestState, TestAction, TestEvent>(store, statePayload) {
    fun event() {
        dispatchAction(TestAction.Event)
    }
}