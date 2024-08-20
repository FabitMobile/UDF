package ru.fabit.udf.store.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ru.fabit.udf.store.coroutines.counter.CounterAction
import ru.fabit.udf.store.coroutines.counter.CounterActionSource
import ru.fabit.udf.store.coroutines.counter.CounterBindActionSource
import ru.fabit.udf.store.coroutines.counter.CounterReducer
import ru.fabit.udf.store.coroutines.counter.CounterState
import ru.fabit.udf.store.coroutines.counter.CounterStore
import ru.fabit.udf.store.coroutines.internal.log
import ru.fabit.udf.store.coroutines.order.OrderAction
import ru.fabit.udf.store.coroutines.order.OrderActionSource
import ru.fabit.udf.store.coroutines.order.OrderBindActionSource
import ru.fabit.udf.store.coroutines.order.OrderReducer
import ru.fabit.udf.store.coroutines.order.OrderState
import ru.fabit.udf.store.coroutines.order.OrderStore
import java.util.concurrent.CopyOnWriteArrayList

class TestStoreTest {

    private val errorHandler = ErrorHandler { t -> t.printStackTrace() }

    private fun store() = TestStore(
        StoreKit.build(
            TestState("init"),
            TestReducer(),
            errorHandler,
            TestAction.BootstrapAction,
            TestActionSource(),
            TestActionSource2(),
            TestActionSource3(),
            TestBootstrapActionSource(),
            TestBindActionSource(),
            TestBindActionSource2(),
            TestBindActionSource3(),
            TestBindActionSource4(),
            TestSideEffect(),
            TestSideEffect2(),
            TestSideEffect3()
        )
    ).apply { start() }

    private fun storeMini() = TestStore(
        StoreKit.build(
            TestState("init"),
            TestReducer(),
            errorHandler,
            TestAction.BootstrapAction,
            TestBindActionSource4()
        )
    ).apply { start() }

    private fun storeCounter(repeat: Int, delay: Long) = CounterStore(
        currentState = CounterState(1),
        reducer = CounterReducer(),
        errorHandler = errorHandler,
        bootStrapAction = CounterAction.BootstrapAction(1),
        bindActionSources = CopyOnWriteArrayList(
            listOf(
                CounterBindActionSource()
            )
        ),
        actionSources = CopyOnWriteArrayList(
            listOf(
                CounterActionSource(repeat, delay)
            )
        )
    ).apply { start() }

    private fun storeOrder(delay: Long) = OrderStore(
        currentState = OrderState("_"),
        reducer = OrderReducer(),
        errorHandler = errorHandler,
        bootStrapAction = OrderAction.NoAction,
        bindActionSources = CopyOnWriteArrayList(listOf(OrderBindActionSource())),
        actionSources = CopyOnWriteArrayList(
            listOf(
                OrderActionSource(delay)
            )
        )
    ).apply { start() }

    @Test
    fun test() = runBlocking {
        val states = mutableListOf<String>()
        val store = store()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.state.collect { state ->
                states.add(state.value)
            }
        }
        delay(100)
        store.dispatchAction(TestAction.NoAction)
        delay(100)
        states.remove("init")
        states.remove("bootstrap action")
        Assert.assertEquals(
            listOf(
                "TestActionSource, 0",
                "TestActionSource2",
                "TestActionSource3, 0",
                "TestBindActionSource",
                "TestBindActionSource2",
                "TestBindActionSource3",
                "TestSideEffect",
                "TestSideEffect2",
                "TestSideEffect3",
                "no action",
                "no action"
            ).sorted(), states.sorted()
        )
        job.cancel()
        store.dispose()
    }

    @Test
    fun test2() = runBlocking {
        val states = mutableListOf<String>()
        val store = store()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.state.collect { state ->
                states.add(state.value)
            }
        }
        delay(3000)
        store.dispatchAction(TestAction.NoAction)
        delay(5_000)
        states.remove("init")
        states.remove("bootstrap action")
        Assert.assertEquals(
            listOf(
                "TestActionSource, 0",
                "TestActionSource, 1",
                "TestActionSource, 2",
                "TestActionSource, 3",
                "TestActionSource, 4",
                "TestActionSource, 5",
                "TestActionSource, 6",
                "TestActionSource, 7",
                "TestActionSource3, 0",
                "TestActionSource3, 1",
                "TestActionSource3, 2",
                "TestActionSource3, 3",
                "TestActionSource2",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource",
                "TestBindActionSource2",
                "TestBindActionSource3",
                "TestBindActionSource3",
                "TestBindActionSource3",
                "TestBindActionSource3",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect",
                "TestSideEffect2",
                "TestSideEffect3",
                "TestSideEffect3",
                "TestSideEffect3",
                "TestSideEffect3",
                "no action",
                "no action"
            ).sorted(),
            states.sorted()
        )
        store.dispose()
        job.cancel()
    }

    @Test
    fun test3() = runBlocking {
        val states = mutableListOf<String>()
        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.state.collect { state ->
                states.add(state.value)
            }
        }
        delay(100)
        store.dispatchAction(TestAction.BindAction4("1"))
        delay(3_000)
        store.dispatchAction(TestAction.BindAction4("2"))
        delay(6_000)
        states.remove("init")
        states.remove("bootstrap action")
        Assert.assertEquals(
            listOf(
                "1",
                "BindActionSource4Completed",
                "BindActionSource4Completed",
                "2",
                "delayBindActionSource4Completed"
            ).sorted(),
            states.sorted()
        )
        store.dispose()
        job.cancel()
    }

    @Test
    fun `test events`() = runBlocking {
        val events = mutableListOf<List<TestEvent>>()

        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.stateWithEvents.collect { stateWithEvents ->
                log(stateWithEvents)
                events.add(stateWithEvents.events)
                log("1232")
            }
        }
        delay(100)
        store.dispatchAction(TestAction.BindAction4("1"))
        delay(100)
        store.dispose()
        job.cancel()
        Assert.assertEquals(
            1,
            events.flatten().size
        )
        Assert.assertEquals(
            TestEvent.Event,
            events.flatten().first()
        )
    }

    @Test
    fun `test events from separate channel`() = runBlocking {
        val events = mutableListOf<List<TestEvent>>()

        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.eventsFlow.collect { event ->
                events.add(event)
            }
        }
        delay(100)
        store.dispatchAction(TestAction.BindAction4("1"))
        delay(100)
        store.dispose()
        job.cancel()
        Assert.assertEquals(
            1,
            events.flatten().size
        )
        Assert.assertEquals(
            TestEvent.Event,
            events.flatten().first()
        )
    }

    /**
     * Эвенты не теряются, если приходят в один момент времени
     */
    @Test
    fun `test fast events`() = runBlocking {
        val events = mutableListOf<List<TestEvent>>()

        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.stateWithEvents.collect { stateWithEvents ->
                events.add(stateWithEvents.events)
            }
        }
        delay(100)
        store.dispatchAction(TestAction.EventAction)
        delay(1)
        store.dispatchAction(TestAction.EventAction)
        delay(1)
        store.dispatchAction(TestAction.OrderEventAction(0))
        delay(100)
        store.dispose()
        job.cancel()
        Assert.assertEquals(
            4,
            events.flatten().size
        )
        Assert.assertEquals(
            TestEvent.Event,
            events.flatten().first()
        )
        Assert.assertTrue(
            events.flatten().last() is TestEvent.OrderEvent
        )
    }

    /**
     * Эвенты не теряются, если приходят в один момент времени
     */
    @Test
    fun `test fast events from separate channel`() = runBlocking {
        val events = mutableListOf<List<TestEvent>>()

        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.eventsFlow.collect { event ->
                events.add(event)
            }
        }
        delay(100)
        store.dispatchAction(TestAction.EventAction)
        delay(1)
        store.dispatchAction(TestAction.EventAction)
        delay(1)
        store.dispatchAction(TestAction.OrderEventAction(0))
        delay(100)
        store.dispose()
        job.cancel()
        Assert.assertEquals(
            4,
            events.flatten().size
        )
        Assert.assertEquals(
            TestEvent.Event,
            events.flatten().first()
        )
        Assert.assertTrue(
            events.flatten().last() is TestEvent.OrderEvent
        )
    }

    @Test
    fun `test events with resubscribe`() = runBlocking {
        val events = mutableListOf<List<TestEvent>>()

        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.stateWithEvents.collect { stateWithEvents ->
                events.add(stateWithEvents.events)
            }
        }
        delay(100)
        job.cancel()
        val job2 = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.stateWithEvents.collect { stateWithEvents ->
                events.add(stateWithEvents.events)
            }
        }
        delay(100)
        store.dispose()
        job2.cancel()
        Assert.assertEquals(
            1,
            events.flatten().size
        )
        Assert.assertEquals(
            TestEvent.Event,
            events.flatten().first()
        )
    }

    @Test
    fun `test events with resubscribe from separate channel`() = runBlocking {
        val events = mutableListOf<List<TestEvent>>()

        val store = storeMini()
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.eventsFlow.collect { event ->
                events.add(event)
            }
        }
        delay(100)
        job.cancel()
        val job2 = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.eventsFlow.collect { event ->
                events.add(event)
            }
        }
        delay(100)
        store.dispose()
        job2.cancel()
        Assert.assertEquals(
            1,
            events.flatten().size
        )
        Assert.assertEquals(
            TestEvent.Event,
            events.flatten().first()
        )
    }

    @Test
    fun increment_with_delay_test() = runBlocking {
        var finishState = 0
        val store = storeCounter(repeat = 10, delay = 100)
        val job = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            store.state.collect { state ->
                finishState = state.value
            }
        }
        delay(2_000)
        Assert.assertEquals(22, finishState)
        store.dispose()
        job.cancel()
    }

    @Test
    fun increment_without_delay_test() = runBlocking {
        var finishState = 0
        val store = storeCounter(repeat = 100, delay = 0)
        val job = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            store.state.collect { state ->
                finishState = state.value
            }
        }
        delay(5_000)
        Assert.assertTrue(finishState > 102)
        store.dispose()
        job.cancel()
    }

    @Test
    fun check_order() = runBlocking {
        var finishState = ""
        val store = storeOrder(delay = 0)
        val job = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            store.state.collect { state ->
                finishState = state.value
            }
        }
        delay(2_000)
        Assert.assertEquals("_0123456789", finishState)
        store.dispose()
        job.cancel()
    }

    @Test
    fun cleaning_event_test() = runBlocking {
        val events = mutableSetOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init"),
                TestReducer(),
                errorHandler,
                TestAction.NoAction
            )
        ).apply { start() }
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.stateWithEvents.collect { stateWithEvents ->
                events.addAll(stateWithEvents.events)
            }
        }
        delay(1000)
        store.dispatchAction(TestAction.EventAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.OrderEventAction(0))
        store.dispatchAction(TestAction.Action("Action1-1"))
        store.dispatchAction(TestAction.Action("Action1-2"))
        store.dispatchAction(TestAction.Action("Action1-3"))
        delay(100)
        Assert.assertEquals(
            2,
            events.count()
        )
        store.dispose()
        job.cancel()
    }

    @Test
    fun cleaning_event_test_from_separate_channel() = runBlocking {
        val events = mutableSetOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init"),
                TestReducer(),
                errorHandler,
                TestAction.NoAction
            )
        ).apply { start() }
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.eventsFlow.collect { event ->
                events.addAll(event)
            }
        }
        delay(1000)
        store.dispatchAction(TestAction.EventAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.OrderEventAction(0))
        store.dispatchAction(TestAction.Action("Action1-1"))
        store.dispatchAction(TestAction.Action("Action1-2"))
        store.dispatchAction(TestAction.Action("Action1-3"))
        delay(100)
        Assert.assertEquals(
            2,
            events.count()
        )
        store.dispose()
        job.cancel()
    }

    @Test
    fun cleaning_state_test() = runBlocking {
        val store = TestStore(
            StoreKit.build(
                TestState("init"),
                TestReducer(),
                errorHandler,
                TestAction.NoAction
            )
        ).apply { start() }
        var textResult = ""
        val jobState = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.state.collect { state ->
                textResult = state.value
            }
        }
        delay(1000)
        store.dispatchAction(TestAction.EventAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.NoAction)
        store.dispatchAction(TestAction.EventAction)
        store.dispatchAction(TestAction.Action("Action1-1"))
        store.dispatchAction(TestAction.Action("Action1-2"))
        store.dispatchAction(TestAction.Action("Action1-3"))
        delay(100)

        Assert.assertEquals(
            "Action1-3",
            textResult
        )
        store.dispose()
        jobState.cancel()
    }

    @Test
    fun order_merging_events1() = runBlocking {
        val events = mutableListOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init"),
                TestReducer(),
                errorHandler,
                TestAction.OrderEventAction(0)
            )
        ).apply { start() }
        delay(100)
        store.dispatchAction(TestAction.OrderEventAction(1))
        delay(100)
        store.dispatchAction(TestAction.OrderEventAction(2))
        delay(100)
        store.dispatchAction(TestAction.OrderEventAction(3))
        delay(100)
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.stateWithEvents.collect { stateWithEvents ->
                events.addAll(stateWithEvents.events)
            }
        }
        delay(100)
        Assert.assertEquals(
            listOf(0, 1, 2, 3),
            events.map { (it as TestEvent.OrderEvent).order }
        )
        store.dispose()
        job.cancel()
    }

    @Test
    fun order_merging_events_from_separate_channel() = runBlocking {
        val events = mutableListOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init"),
                TestReducer(),
                errorHandler,
                TestAction.OrderEventAction(0)
            )
        ).apply { start() }
        delay(100)
        store.dispatchAction(TestAction.OrderEventAction(1))
        delay(100)
        store.dispatchAction(TestAction.OrderEventAction(2))
        delay(100)
        store.dispatchAction(TestAction.OrderEventAction(3))
        delay(100)
        val job = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            store.eventsFlow.collect { event ->
                events.addAll(event)
            }
        }
        delay(100)
        Assert.assertEquals(
            listOf(0, 1, 2, 3),
            events.map { (it as TestEvent.OrderEvent).order }
        )
        store.dispose()
        job.cancel()
    }
}