package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.flowOf

class TestActionSource2 : ActionSource<TestAction>(
    source = {
        flowOf(TestAction.Action2("TestActionSource2"))
    },
    error = {
        TestAction.Action2(it.toString())
    }
)