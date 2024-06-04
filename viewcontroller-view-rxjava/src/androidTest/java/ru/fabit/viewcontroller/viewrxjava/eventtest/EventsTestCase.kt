package ru.fabit.viewcontroller.viewrxjava.eventtest

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ru.fabit.udf.store.ErrorHandler
import ru.fabit.udf.store.StoreKit
import ru.fabit.viewcontroller.viewrxjava.EventsView
import ru.fabit.viewcontroller.viewrxjava.awaitDebug
import ru.fabit.viewcontroller.viewrxjava.teststore.TestAction
import ru.fabit.viewcontroller.viewrxjava.teststore.TestActionSource
import ru.fabit.viewcontroller.viewrxjava.teststore.TestActionSource2
import ru.fabit.viewcontroller.viewrxjava.teststore.TestActionSource3
import ru.fabit.viewcontroller.viewrxjava.teststore.TestEvent
import ru.fabit.viewcontroller.viewrxjava.teststore.TestReducer
import ru.fabit.viewcontroller.viewrxjava.teststore.TestState
import ru.fabit.viewcontroller.viewrxjava.teststore.TestStore

@RunWith(AndroidJUnit4::class)
class EventsTestCase : TestCase() {

    private lateinit var scenario: FragmentScenario<TestFragment>

    private val errorHandler = ErrorHandler { t -> t.printStackTrace() }

    /**
     * получили все отправленные эвенты
     */
    private val resultEvents = listOf(
        TestEvent.Event,
        TestEvent.Event,
        TestEvent.EventValue(0),
        TestEvent.EventValue(1),
        TestEvent.EventValue(2),
        TestEvent.EventValue(3),
        TestEvent.EventValue(4),
        TestEvent.EventValue(5),
        TestEvent.EventValue(6),
        TestEvent.EventValue(7),
        TestEvent.EventValue(8),
        TestEvent.EventValue(9)
    )

    /**
     * несколько стейтов потерялось когда фрагмен был на паузе
     */
    private val resultStates = listOf(
        TestState(-1),
        TestState(666),
        TestState(0),
        TestState(1),
        TestState(5),
        TestState(6),
        TestState(7777),
        TestState(7),
        TestState(8),
        TestState(9)
    )

    private val events = mutableListOf<TestEvent>()
    private val states = mutableListOf<TestState>()

    private var eventsView = EventsView<TestState, TestEvent> { s, e, p ->
        events.addAll(e)
        states.add(s)
    }

    @Test
    fun startActivity() = before {
        val store = TestStore(
            StoreKit.build(
                TestState(-1),
                TestAction.Init,
                TestReducer,
                errorHandler,
                TestActionSource(),
                TestActionSource2(),
                TestActionSource3()
            )
        )
        val viewController = EventViewController(store)
        scenario = launchFragmentInContainer {
            TestFragment(viewController, eventsView)
        }
    }.after {}.run {
        step("Launch TestFragment") {
            awaitDebug(2000)
            scenario.moveToState(Lifecycle.Event.ON_PAUSE.targetState)
            awaitDebug(4000)
            scenario.moveToState(Lifecycle.Event.ON_RESUME.targetState)
            awaitDebug(4000)
            assertEquals(resultEvents, events)
            assertEquals(resultStates, states)
        }
    }
}