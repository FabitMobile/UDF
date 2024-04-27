package ru.fabit.udf.store.teststore

import ru.fabit.udf.store.EventsReducer

object TestReducer : EventsReducer<TestState, TestAction, TestEvent>() {
    override fun TestState.reduce(action: TestAction): TestState {
        return when (action) {
            is TestAction.Value -> copy(
                value = action.value
            )

            is TestAction.Event -> copy(
                value = action.value
            ) + TestEvent.Event

            else -> copy()
        }
    }
}