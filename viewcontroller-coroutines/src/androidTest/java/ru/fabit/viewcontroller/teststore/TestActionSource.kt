package ru.fabit.viewcontroller.teststore

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.fabit.udf.store.coroutines.ActionSource
import ru.fabit.viewcontroller.interval
import java.util.concurrent.TimeUnit

class TestActionSource : ActionSource<TestAction>(
    source = {
        flow {
            interval(1000, TimeUnit.MILLISECONDS).collect {
                emit(TestAction.EventValue(it.toInt()))
            }
        }
    },
    error = {
        TestAction.Init
    }
)

class TestActionSource2 : ActionSource<TestAction>(
    source = {
        flow {
            emit(TestAction.Event)
            emit(TestAction.Event)
        }
    },
    error = {
        TestAction.Init
    }
)

class TestActionSource3 : ActionSource<TestAction>(
    source = {
        flow {
            delay(666)
            emit(TestAction.Value(666))
            delay(7777)
            emit(TestAction.Value(7777))
        }
    },
    error = {
        TestAction.Init
    }
)