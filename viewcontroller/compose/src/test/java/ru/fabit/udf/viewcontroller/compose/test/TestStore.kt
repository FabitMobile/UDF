package ru.fabit.udf.viewcontroller.compose.test

import ru.fabit.udf.store.ActionHandler
import ru.fabit.udf.store.ActionSource
import ru.fabit.udf.store.BindActionSource
import ru.fabit.udf.store.ErrorHandler
import ru.fabit.udf.store.EventsStore
import ru.fabit.udf.store.SideEffect
import java.util.concurrent.CopyOnWriteArrayList

class TestStore(
    currentState: TestState,
    reducer: TestReducer,
    errorHandler: ErrorHandler,
    bootStrapAction: TestAction,
    actionHandlers: Iterable<ActionHandler<TestState, TestAction>> = CopyOnWriteArrayList(),
    actionSources: Iterable<ActionSource<TestAction>> = CopyOnWriteArrayList(),
    bindActionSources: Iterable<BindActionSource<TestState, TestAction>> = CopyOnWriteArrayList(),
    sideEffects: Iterable<SideEffect<TestState, TestAction>> = CopyOnWriteArrayList()
) : EventsStore<TestState, TestAction, TestEvent>(
    currentState,
    reducer,
    errorHandler,
    bootStrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
)