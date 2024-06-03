package ru.fabit.viewcontroller.viewrxjava.payloadtest

import ru.fabit.viewcontroller.viewrxjava.StatePayload
import ru.fabit.viewcontroller.viewrxjava.teststore.TestState

class TestStatePayload : StatePayload<TestState> {
    override fun payload(prevState: TestState?, newState: TestState): Any? {
        val changes: MutableList<Change> = mutableListOf()

        if (newState.value == -1)
            changes.add(Change.TestChange)

        return changes
    }
}