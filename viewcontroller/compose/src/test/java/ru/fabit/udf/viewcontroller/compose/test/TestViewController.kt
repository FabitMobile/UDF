package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.viewcontroller.compose.EventsViewController

class TestViewController(
    store: TestStore
) : EventsViewController<TestState, TestAction, TestEvent>(store) {

    fun event() {
        dispatchAction(TestAction.EventAction)
    }

    fun noAction() {
        dispatchAction(TestAction.NoAction)
    }

    fun action(text: String) {
        dispatchAction(TestAction.Action(text))
    }
}