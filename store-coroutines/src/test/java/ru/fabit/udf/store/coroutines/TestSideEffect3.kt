package ru.fabit.udf.store.coroutines

class TestSideEffect3 : SideEffect<TestState, TestAction>(
    query = { _, action -> action is TestAction.BindAction3 },
    effect = { _, _ ->
        TestAction.SideAction3("TestSideEffect3")
    },
    error = { TestAction.SideAction3("TestSideEffect3") }
)