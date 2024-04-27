package ru.fabit.viewcontroller.viewrxjava.teststore

import ru.fabit.udf.store.EventsReducer

object TestReducer : EventsReducer<TestState, TestAction, TestEvent>() {
    override fun TestState.reduce(action: TestAction): TestState {
        return when (action) {
            is TestAction.Value -> copy(
                value = action.value
            )

            is TestAction.EventValue -> copy(
                value = action.value
            ) + TestEvent.EventValue(action.value)

            is TestAction.Event -> copy() + TestEvent.Event

            else -> copy()
        }
    }
}