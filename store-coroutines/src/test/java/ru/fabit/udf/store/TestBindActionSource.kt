package ru.fabit.udf.store

import kotlinx.coroutines.flow.flowOf
import ru.fabit.udf.store.coroutines.BindActionSource

class TestBindActionSource : BindActionSource<TestState, TestAction>(
    query = { _, action -> action is TestAction.Action },
    source = { _, _ ->
        flowOf(TestAction.BindAction("TestBindActionSource"))
    },
    error = { TestAction.BindAction("TestBindActionSource") }
)