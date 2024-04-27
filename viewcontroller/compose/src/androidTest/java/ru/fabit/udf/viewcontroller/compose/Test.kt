package ru.fabit.udf.viewcontroller.compose

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.delay
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import ru.fabit.udf.store.ErrorHandler
import ru.fabit.udf.store.StoreKit
import ru.fabit.udf.viewcontroller.compose.internal.log
import ru.fabit.udf.viewcontroller.compose.test.TestAction
import ru.fabit.udf.viewcontroller.compose.test.TestActionSource
import ru.fabit.udf.viewcontroller.compose.test.TestEvent
import ru.fabit.udf.viewcontroller.compose.test.TestReducer
import ru.fabit.udf.viewcontroller.compose.test.TestState
import ru.fabit.udf.viewcontroller.compose.test.TestStore
import ru.fabit.udf.viewcontroller.compose.test.TestViewController

class Test {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val eventOnIndex = 40

    /**
     * первый эвент с BootstrapAction - TestEvent.Event
     * второй эвент через 100мс TestEvent.Event2
     * третий эвент на $eventOnIndex экшене - TestEvent.Event2
     */
    @Test
    fun test_events() {
        var lastEvents: List<TestEvent?> = listOf()

        var firstEvent: TestEvent? = null
        var secondEvent: TestEvent? = null
        var thirdEvent: TestEvent? = null

        val viewController = TestViewController(store())

        composeTestRule.setContent {
            val state = viewController.renderState()
            val events = viewController.renderEvents()

            lastEvents = events.value
            log("Test events = ${events.value}")
            log("Test state = ${state.value}")

            if (firstEvent == null) {
                firstEvent = events.value?.firstOrNull()
                secondEvent = events.value?.getOrNull(1)
                thirdEvent = events.value?.getOrNull(2)
            } else {
                if (secondEvent == null) {
                    secondEvent = events.value?.firstOrNull()
                    thirdEvent = events.value?.getOrNull(1)
                } else {
                    if (thirdEvent == null) {
                        thirdEvent = events.value?.firstOrNull()
                    }
                }
            }
        }

        try {
            composeTestRule.waitUntil(5000) {
                firstEvent != null && secondEvent != null && thirdEvent != null && lastEvents?.isEmpty() == true
            }
        } catch (_: ComposeTimeoutException) {
            Assert.assertTrue(false)
        } finally {
            log("Test firstEvent=$firstEvent")
            log("Test secondEvent=$secondEvent")
            log("Test thirdEvent=$thirdEvent")

            Assert.assertTrue(firstEvent is TestEvent.BootstrapEvent)
            Assert.assertTrue(secondEvent is TestEvent.Event1)
            Assert.assertTrue(thirdEvent is TestEvent.Event1)
            Assert.assertNotEquals(secondEvent, firstEvent)
            Assert.assertEquals(secondEvent, thirdEvent)
        }
    }

    /**
     * пропускаем экшены EventAction через viewController
     * получаем их
     * ожидаем что будет отработан алгоритм накопления эвентов до появления подписчика и все евенты придут разом
     */
    @Test
    fun test_events2() {
        val sentEvents = mutableListOf(
            TestEvent.Event1(),
            TestEvent.Event2(),
            TestEvent.Event3(),
            TestEvent.Event1(),
            TestEvent.Event1(),
            TestEvent.Event3()
        )
        val handledEvents = mutableListOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init", ""),
                TestReducer,
                errorHandler
            )
        )
        val viewController = TestViewController(store)
        composeTestRule.setContent {
            val state = viewController.renderState()
            val events = viewController.renderEvents()
            LaunchedEffect("Unit2") {
                sentEvents.onEach {
                    viewController.event(it)
                }
            }
            events.value.let { handledEvents.addAll(it) }
            log("Test events = ${events.value}")
            log("Test state = ${state.value}")
        }

        try {
            composeTestRule.waitUntil(5000) { false }
        } catch (_: ComposeTimeoutException) {
        } finally {
            log("Test sentEvents = $sentEvents")
            log("Test handledEvents = $handledEvents")
            Assert.assertTrue(sentEvents == handledEvents)
        }
    }

    /**
     * ждем delay(3000), чтобы успели подписаться на store
     * пропускаем экшены EventAction через viewController
     * получаем их
     * ожидаем что алгоритм накопления эвентов до появления подписчика не будет задействован
     */
    @Test
    fun test_events3() {
        val sentEvents = mutableListOf(
            TestEvent.Event1(),
            TestEvent.Event2(),
            TestEvent.Event3(),
            TestEvent.Event1(),
            TestEvent.Event1(),
            TestEvent.Event3()
        )
        val handledEvents = mutableListOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init", ""),
                TestReducer,
                errorHandler
            )
        )
        val viewController = TestViewController(store)
        composeTestRule.setContent {
            val state = viewController.renderState()
            val events = viewController.renderEvents()
            LaunchedEffect("Unit2") {
                delay(3000)
                sentEvents.onEach {
                    viewController.event(it)
                }
            }
            events.value.let { handledEvents.addAll(it) }
            log("Test events = ${events.value}")
            log("Test state = ${state.value}")
        }

        try {
            composeTestRule.waitUntil(5000) { false }
        } catch (_: ComposeTimeoutException) {
        } finally {
            log("Test sentEvents = $sentEvents")
            log("Test handledEvents = $handledEvents")
            Assert.assertTrue(sentEvents == handledEvents)
        }
    }

    /**
     * пропускаем экшены ListEventAction через viewController несколько раз countAction
     * ожидаем что все сгенерированные эвенты будут доставлены
     */
    @Test
    fun test_events4() {
        val countAction = 3
        val sentEvents = mutableListOf(
            TestEvent.Event1(),
            TestEvent.Event1(),
            TestEvent.Event2()
        )
        val expectedEvents = mutableListOf<TestEvent>()
        repeat(countAction) {
            expectedEvents.addAll(sentEvents)
        }
        val handledEvents = mutableListOf<TestEvent>()
        val store = TestStore(
            StoreKit.build(
                TestState("init", ""),
                TestReducer,
                errorHandler
            )
        )
        val viewController = TestViewController(store)
        composeTestRule.setContent {
            val state = viewController.renderState()
            val events = viewController.renderEvents()
            LaunchedEffect("Unit2") {
                delay(3000)
                repeat(countAction) {
                    viewController.listEvent(sentEvents)
                }
            }
            events.value.let { handledEvents.addAll(it) }
            log("Test events = ${events.value}")
            log("Test state = ${state.value}")
        }

        try {
            composeTestRule.waitUntil(5000) { false }
        } catch (_: ComposeTimeoutException) {
        } finally {
            log("Test expectedEvents = $expectedEvents")
            log("Test handledEvents = $handledEvents")
            Assert.assertEquals(expectedEvents, handledEvents)
        }
    }

    private val errorHandler =
        object : ErrorHandler {
            override fun handle(t: Throwable) {
                t.printStackTrace()
            }
        }

    private fun store() = TestStore(
        StoreKit.build(
            TestState("init", ""),
            TestReducer,
            errorHandler,
            TestAction.BootstrapAction,
            TestActionSource(eventOnIndex)
        )
    )
}