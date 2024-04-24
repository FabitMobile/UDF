package ru.fabit.udf.viewcontroller.compose.test

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.fabit.udf.store.ActionSource

class TestActionSource(private val eventOnIndex: Int) : ActionSource<TestAction>(
    source = {
        flow {
            var i = 0
            delay(100)
            emit(TestAction.EventAction)
            while (true) {
                i++
                delay(100)
                val action = if (i == eventOnIndex)
                    TestAction.EventAction
                else
                    TestAction.Action("TestActionSource, $i")
                emit(action)
            }
        }
    },
    error = {
        TestAction.NoAction
    }
)