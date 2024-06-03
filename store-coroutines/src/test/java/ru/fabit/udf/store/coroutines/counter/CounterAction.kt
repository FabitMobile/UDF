package ru.fabit.udf.store.coroutines.counter

sealed class CounterAction {

    data class BootstrapAction(val value: Int) : CounterAction() {
        override fun toString(): String {
            return "BootstrapAction"
        }
    }

    object NoAction : CounterAction() {
        override fun toString(): String {
            return "NoAction"
        }
    }

    data class Action(val value: Int) : CounterAction()

    data class BindAction(val value: Int) : CounterAction()
}