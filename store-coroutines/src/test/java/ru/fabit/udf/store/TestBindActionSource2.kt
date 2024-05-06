package ru.fabit.udf.store

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource2 : BindActionSource<TestState, TestAction>(
    query = { _, action -> action is TestAction.Action2 },
    source = { _, _ ->
        flowOf(TestAction.BindAction2("TestBindActionSource2"))
    },
    error = { TestAction.BindAction2("TestBindActionSource2") }
)