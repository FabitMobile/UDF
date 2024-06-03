package ru.fabit.udf.store.coroutines

class TestSideEffect : SideEffect<TestState, TestAction>(
    query = { _, action -> action is TestAction.BindAction },
    effect = { _, _ ->
        TestAction.SideAction("TestSideEffect")
    },
    error = { TestAction.SideAction(it.toString()) }
)