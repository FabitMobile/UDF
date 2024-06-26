package ru.fabit.udf.viewcontroller.compose.test

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.fabit.udf.store.coroutines.ActionSource

class TestActionSource(private val eventOnIndex: Int) : ActionSource<TestAction>(
    source = {
        flow {
            var i = 0
            delay(100)
            emit(TestAction.EventAction(TestEvent.Event1()))
            while (true) {
                i++
                delay(100)
                val action = if (i == eventOnIndex)
                    TestAction.EventAction(TestEvent.Event1())
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