package ru.fabit.udf.store.coroutines

sealed class TestAction {
    object NoAction : TestAction() {
        override fun toString(): String {
            return "NoAction"
        }
    }

    object BootstrapAction : TestAction() {
        override fun toString(): String {
            return "BootstrapAction"
        }
    }

    data class Action(val value: String) : TestAction()

    data class Action2(val value: String) : TestAction()

    data class Action3(val value: String) : TestAction()

    data class BindAction(val value: String) : TestAction()

    data class BindAction2(val value: String) : TestAction()

    data class BindAction3(val value: String) : TestAction()

    data class BindAction4(val value: String) : TestAction()

    data class SideAction(val value: String) : TestAction()

    data class SideAction2(val value: String) : TestAction()

    data class SideAction3(val value: String) : TestAction()

    object EventAction : TestAction()

    data class OrderEventAction(val order: Int) : TestAction()
}