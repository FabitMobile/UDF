package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.store.coroutines.EventsReducer

object TestReducer : EventsReducer<TestState, TestAction, TestEvent>() {
    override fun TestState.reduce(action: TestAction): TestState {
        return when (action) {
            is TestAction.NoAction -> copy(
                value = "no action"
            )

            is TestAction.BootstrapAction -> copy(
                value = "bootstrap action",
            ) + TestEvent.BootstrapEvent

            is TestAction.Action -> copy(
                value = action.value,
                value2 = value2 + action.value
            )

            is TestAction.EventAction -> {
                copy(
                    value = "event action ${action.event}",
                ) + action.event
            }

            is TestAction.ListEventAction -> {
                copy(
                    value = "list event action",
                ) + action.events
            }
        }
    }
}