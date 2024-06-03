package ru.fabit.udf.store

import ru.fabit.udf.store.coroutines.SideEffect

class TestSideEffect2 : SideEffect<TestState, TestAction>(
    query = { _, action -> action is TestAction.BindAction2 },
    effect = { _, _ ->
        TestAction.SideAction2("TestSideEffect2")
    },
    error = { TestAction.SideAction2("TestSideEffect2") }
)