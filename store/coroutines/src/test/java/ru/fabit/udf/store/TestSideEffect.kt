package ru.fabit.udf.store

class TestSideEffect : SideEffect<TestState, TestAction>(
    query = { _, action -> action is TestAction.BindAction },
    effect = { _, _ ->
        TestAction.SideAction("TestSideEffect")
    },
    error = { TestAction.SideAction(it.toString()) }
)