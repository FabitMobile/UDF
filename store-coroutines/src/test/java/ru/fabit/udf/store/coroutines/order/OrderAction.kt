package ru.fabit.udf.store.coroutines.order

sealed class OrderAction {

    data class BootstrapAction(val value: String) : OrderAction() {
        override fun toString(): String {
            return "BootstrapAction"
        }
    }

    object NoAction : OrderAction() {
        override fun toString(): String {
            return "NoAction"
        }
    }

    data class Action(val value: String) : OrderAction()

    data class BindAction(val value: String) : OrderAction()
}