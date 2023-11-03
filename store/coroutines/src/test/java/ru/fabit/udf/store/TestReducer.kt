package ru.fabit.udf.store

class TestReducer : Reducer<TestState, TestAction> {
    override fun TestState.reduce(action: TestAction): TestState {
        return when (action) {
            is TestAction.NoAction -> copy(
                value = "no action"
            )

            is TestAction.BootstrapAction -> copy(
                value = "bootstrap action",
                events = events + TestEvent.Event
            )

            is TestAction.Action -> copy(
                value = action.value
            )

            is TestAction.Action2 -> copy(
                value = action.value
            )

            is TestAction.Action3 -> copy(
                value = action.value
            )

            is TestAction.BindAction -> copy(
                value = action.value
            )

            is TestAction.BindAction2 -> copy(
                value = action.value
            )

            is TestAction.BindAction3 -> copy(
                value = action.value
            )

            is TestAction.BindAction4 -> copy(
                value = action.value
            )

            is TestAction.SideAction -> copy(
                value = action.value
            )

            is TestAction.SideAction2 -> copy(
                value = action.value
            )

            is TestAction.SideAction3 -> copy(
                value = action.value
            )

            is TestAction.EventAction -> copy(
                value = "event action",
                events = events + TestEvent.Event2
            )

            is TestAction.OrderEventAction -> copy(
                value = "order event action ${action.order}",
                events = events + TestEvent.OrderEvent(action.order)
            )

            else -> copy()
        }
    }
}