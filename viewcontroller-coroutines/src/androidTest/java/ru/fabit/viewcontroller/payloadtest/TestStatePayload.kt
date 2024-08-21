package ru.fabit.viewcontroller.payloadtest

import ru.fabit.viewcontroller.coroutines.StatePayload
import ru.fabit.viewcontroller.teststore.TestState

class TestStatePayload : StatePayload<TestState> {
    override fun payload(prevState: TestState?, newState: TestState): Any? {
        val changes: MutableList<Change> = mutableListOf()

        if (newState.value == -1)
            changes.add(Change.TestChange)

        return changes
    }
}