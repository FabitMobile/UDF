package ru.fabit.udf.store

import kotlinx.coroutines.flow.flowOf
import ru.fabit.udf.store.coroutines.ActionSource

class TestActionSource2 : ActionSource<TestAction>(
    source = {
        flowOf(TestAction.Action2("TestActionSource2"))
    },
    error = {
        TestAction.Action2(it.toString())
    }
)