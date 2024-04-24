package ru.fabit.udf.viewcontroller.compose

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.junit4.createComposeRule
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
        var lastEvents: List<TestEvent>? = listOf()

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
            Assert.assertTrue(secondEvent is TestEvent.Event)
            Assert.assertTrue(thirdEvent is TestEvent.Event)
            Assert.assertNotEquals(secondEvent, firstEvent)
            Assert.assertEquals(secondEvent, thirdEvent)
        }
    }

    /**
     * пропускаем три экшена EventAction через viewController
     * получаем их
     */
    @Test
    fun test_events2() {
        val value = mutableListOf<TestEvent>()

        var firstEvent: TestEvent? = null
        var secondEvent: TestEvent? = null
        var thirdEvent: TestEvent? = null

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

            LaunchedEffect(Unit) {
                viewController.event()
                viewController.event()
                viewController.event()
            }

            events.value?.let { value.addAll(it) }
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
                firstEvent != null && secondEvent != null && value.size == 3
            }
        } catch (_: ComposeTimeoutException) {
            Assert.assertTrue(false)
        } finally {
            log("Test firstEvent=$firstEvent")
            log("Test secondEvent=$secondEvent")
            log("Test thirdEvent=$thirdEvent")

            Assert.assertTrue(firstEvent is TestEvent.Event)
            Assert.assertTrue(secondEvent is TestEvent.Event)
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