package ru.fabit.udf.store

import org.junit.Assert
import org.junit.Test
import ru.fabit.udf.store.teststore.TestAction
import ru.fabit.udf.store.teststore.TestActionSource3
import ru.fabit.udf.store.teststore.TestBindActionSource4
import ru.fabit.udf.store.teststore.TestEvent
import ru.fabit.udf.store.teststore.TestReducer
import ru.fabit.udf.store.teststore.TestState
import ru.fabit.udf.store.teststore.TestStore

class Test {
    private val errorHandler = ErrorHandler { t -> t.printStackTrace() }

    @Test
    fun test() {
        val store = TestStore(
            StoreKit.build(
                TestState(-1),
                TestAction.Init,
                TestReducer,
                errorHandler,
                TestActionSource3(),
                TestBindActionSource4()
            )
        )
        store.start()
        val test = store.state.test()
        store.dispatchAction(TestAction.Value(100))
        test.awaitCount(3)
        test.assertNoErrors()
        test.assertValueAt(0, TestState(-1))
        test.assertValueAt(1, TestState(100))
        test.assertValueAt(2, TestState(0))
    }

    @Test
    fun test_event() {
        val store = TestStore(
            StoreKit.build(
                TestState(-1),
                TestAction.Init,
                TestReducer,
                errorHandler,
                TestBindActionSource4()
            )
        )
        store.start()
        val states = mutableListOf<TestState>()
        val testState = store.state.doOnNext {
            states.add(it)
        }.test()
        val testEvents = store.stateWithEvents.test()
        store.dispatchAction(TestAction.Event(5))
        testState.awaitCount(2)
        testState.assertNoErrors()
        Assert.assertEquals(states, listOf(TestState(-1), TestState(5)))

        testEvents.awaitCount(2)
        testEvents.assertNoErrors()
        testEvents.assertValueAt(0, StateWithEvents(TestState(-1), listOf()))
        testEvents.assertValueAt(1, StateWithEvents(TestState(5), listOf(TestEvent.Event)))
    }
}