package ru.fabit.udf.store

class TestSideEffect2 : SideEffect<TestState, TestAction>(
    query = { _, action -> action is TestAction.BindAction2 },
    effect = { _, _ ->
        TestAction.SideAction2("TestSideEffect2")
    },
    error = { TestAction.SideAction2("TestSideEffect2") }
)