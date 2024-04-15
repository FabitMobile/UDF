package ru.fabit.udf.viewcontroller.compose

import androidx.compose.runtime.Composable
import org.junit.Assert
import org.junit.Test
import ru.fabit.udf.store.ErrorHandler
import ru.fabit.udf.viewcontroller.compose.test.TestAction
import ru.fabit.udf.viewcontroller.compose.test.TestReducer
import ru.fabit.udf.viewcontroller.compose.test.TestState
import ru.fabit.udf.viewcontroller.compose.test.TestStore
import ru.fabit.udf.viewcontroller.compose.test.TestViewController

class Test {

    private val errorHandler =
        object : ErrorHandler {
            override fun handle(t: Throwable) {
                println(t)
            }
        }

    private fun store() = TestStore(
        currentState = TestState("init"),
        reducer = TestReducer(),
        errorHandler = errorHandler,
        bootStrapAction = TestAction.BootstrapAction,
        actionHandlers = listOf(),
        actionSources = listOf(),
        bindActionSources = listOf(),
    ).apply { start() }

    @Test
    @Composable
    fun test() {
        val store = store()
        val viewController = TestViewController(store)
        val event = viewController.renderEvent()
        viewController.event()
        viewController.noAction()

        Assert.assertEquals(
            "",
            event.value
        )

        store.dispose()
    }
}