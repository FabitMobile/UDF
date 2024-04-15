package ru.fabit.udf.viewcontroller.compose.test

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

    object EventAction : TestAction()
}