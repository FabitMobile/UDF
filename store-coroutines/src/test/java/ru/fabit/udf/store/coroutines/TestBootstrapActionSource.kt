package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

class TestBootstrapActionSource : BindActionSource<TestState, TestAction>(
    query = { _, action -> action is TestAction.BootstrapAction },
    source = { _, _ ->
        delay(100)
        flowOf(TestAction.NoAction)
    },
    error = { TestAction.BindAction("TestBindActionSource") }
)