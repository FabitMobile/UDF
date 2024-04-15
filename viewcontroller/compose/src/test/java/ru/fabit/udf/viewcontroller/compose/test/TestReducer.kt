package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.store.EventsReducer

class TestReducer : EventsReducer<TestState, TestAction, TestEvent>() {
    override fun TestState.reduce(action: TestAction): TestState {
        return when (action) {
            is TestAction.NoAction -> copy(
                value = "no action"
            )

            is TestAction.BootstrapAction -> copy(
                value = "bootstrap action",
            ) + TestEvent.Event()

            is TestAction.Action -> copy(
                value = action.value
            )

            is TestAction.EventAction -> copy(
                value = "event action",
            ) + TestEvent.Event2()

            else -> copy()
        }
    }
}