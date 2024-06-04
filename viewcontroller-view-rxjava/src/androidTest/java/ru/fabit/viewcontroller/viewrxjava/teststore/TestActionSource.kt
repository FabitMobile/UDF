package ru.fabit.viewcontroller.viewrxjava.teststore

import io.reactivex.Observable
import ru.fabit.udf.store.ActionSource
import ru.fabit.udf.store.BindActionSource
import ru.fabit.udf.store.actionIs
import java.util.concurrent.TimeUnit

class TestActionSource : ActionSource<TestAction>(
    source = {
        Observable.interval(1000, TimeUnit.MILLISECONDS).map {
            TestAction.EventValue(it.toInt())
        }
    },
    error = {
        TestAction.Init
    }
)

class TestActionSource2 : ActionSource<TestAction>(
    source = Create {
        onNext(TestAction.Event)
        onNext(TestAction.Event)
        onComplete()
    },
    error = {
        TestAction.Init
    }
)

class TestActionSource3 : ActionSource<TestAction>(
    source = Defer {
        Observable.merge(
            Observable.just(TestAction.Value(666)).delay(666, TimeUnit.MILLISECONDS),
            Observable.just(TestAction.Value(7777)).delay(7777, TimeUnit.MILLISECONDS)
        )
    },
    error = {
        TestAction.Init
    }
)

class TestBindActionSource : BindActionSource<TestState, TestAction>(
    query = { _, _ -> true },
    source = { state, action ->
        Observable.never()
    },
    error = {
        TestAction.Init
    }
)

class TestBindActionSource2 : BindActionSource<TestState, TestAction>(
    query = { _, _ -> true },
    source = Create { state, action ->
        onNext(TestAction.Value(0))
        onComplete()
    },
    error = {
        TestAction.Init
    }
)

class TestBindActionSource3 : BindActionSource<TestState, TestAction>(
    query = { _, _ -> true },
    source = Defer { state, action ->
        Observable.just(TestAction.Init)
    },
    error = {
        TestAction.Init
    }
)

class TestBindActionSource4 : BindActionSource<TestState, TestAction>(
    query = actionIs<TestAction.Value>(),
    source = Create { state, action ->
        onNext(TestAction.Value(0))
        onComplete()
    },
    error = {
        TestAction.Init
    }
)