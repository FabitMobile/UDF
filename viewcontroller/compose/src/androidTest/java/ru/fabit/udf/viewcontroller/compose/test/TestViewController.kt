package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.viewcontroller.compose.EventsViewController

class TestViewController(
    store: TestStore
) : EventsViewController<TestState, TestAction, TestEvent>(store) {

    fun event(event: TestEvent) {
        dispatchAction(TestAction.EventAction(event))
    }

    fun listEvent(events: List<TestEvent>) {
        dispatchAction(TestAction.ListEventAction(events))
    }

    fun noAction() {
        dispatchAction(TestAction.NoAction)
    }

    fun action(text: String) {
        dispatchAction(TestAction.Action(text))
    }
}