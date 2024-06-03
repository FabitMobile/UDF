package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource : BindActionSource<TestState, TestAction>(
    query = { _, action -> action is TestAction.Action },
    source = { _, _ ->
        flowOf(TestAction.BindAction("TestBindActionSource"))
    },
    error = { TestAction.BindAction("TestBindActionSource") }
)