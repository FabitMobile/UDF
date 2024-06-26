package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.flow

class TestActionSource3 : ActionSource<TestAction>(
    source = {
        flow {
            var i = 0
            emit(TestAction.Action3("TestActionSource3, $i"))
            while (true) {
                i++
                kotlinx.coroutines.delay(2000)
                emit(TestAction.Action3("TestActionSource3, $i"))
            }
        }
    },
    error = {
        TestAction.Action3(it.toString())
    }
)