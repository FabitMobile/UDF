package ru.fabit.viewcontroller.viewrxjava.payloadtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.fabit.viewcontroller.core.EventsView
import ru.fabit.viewcontroller.viewrxjava.internal.log
import ru.fabit.viewcontroller.viewrxjava.registerViewController
import ru.fabit.viewcontroller.viewrxjava.teststore.TestEvent
import ru.fabit.viewcontroller.viewrxjava.teststore.TestState

class PayloadFragment(
    private val viewController: PayloadViewController,
    private val eventsView: EventsView<TestState, TestEvent>
) : Fragment(), EventsView<TestState, TestEvent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewController(viewController)
    }

    override fun renderState(state: TestState, events: List<TestEvent>, payload: Any?) {
        log("_________ state=$state; events=$events; payload=$payload")
        eventsView.renderState(state, events, payload)
    }
}