package ru.fabit.udf.store

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.fabit.udf.store.coroutines.BindActionSource

class TestBindActionSource4 : BindActionSource<TestState, TestAction>(
    query = { _, action -> action is TestAction.BindAction4 },
    source = { _, action ->
        flow {
            action as TestAction.BindAction4
            emit(TestAction.Action("BindActionSource4Completed"))
            delay(5000)
            //одиноковые значения не обрабатываются
            emit(TestAction.Action("delayBindActionSource4Completed"))
        }
    },
    error = { TestAction.BindAction("TestBindActionSource4") }
)