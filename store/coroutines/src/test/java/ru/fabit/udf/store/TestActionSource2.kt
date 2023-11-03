package ru.fabit.udf.store

import kotlinx.coroutines.flow.flowOf

class TestActionSource2 : ru.fabit.udf.store.ActionSource<TestAction>(
    source = {
        flowOf(TestAction.Action2("TestActionSource2"))
    },
    error = {
        TestAction.Action2(it.toString())
    }
)