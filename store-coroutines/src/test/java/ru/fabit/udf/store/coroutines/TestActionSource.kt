package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.flow.flow

class TestActionSource : ActionSource<TestAction>(
    source = {
        flow {
            var i = 0
            emit(TestAction.Action("TestActionSource, $i"))
            while (true) {
                i++
                kotlinx.coroutines.delay(1000)
                val action = TestAction.Action("TestActionSource, $i")
                emit(action)

            }
        }
    },
    error = {
        TestAction.NoAction
    }
)