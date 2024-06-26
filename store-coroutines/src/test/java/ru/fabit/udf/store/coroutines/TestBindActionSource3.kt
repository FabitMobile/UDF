package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.flowOf

class TestBindActionSource3 : BindActionSource<TestState, TestAction>(
    query = { _, action -> action is TestAction.Action3 },
    source = { _, _ ->
        flowOf(TestAction.BindAction3("TestBindActionSource3"))
    },
    error = { TestAction.BindAction3("TestBindActionSource3") }
)