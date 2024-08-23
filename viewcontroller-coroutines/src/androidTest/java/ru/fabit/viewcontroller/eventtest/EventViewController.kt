package ru.fabit.viewcontroller.eventtest

import ru.fabit.viewcontroller.coroutines.EventsViewController
import ru.fabit.viewcontroller.teststore.TestAction
import ru.fabit.viewcontroller.teststore.TestEvent
import ru.fabit.viewcontroller.teststore.TestState
import ru.fabit.viewcontroller.teststore.TestStore

class EventViewController(store: TestStore) :
    EventsViewController<TestState, TestAction, TestEvent>(store) {
    fun event() {
        dispatchAction(TestAction.Event)
    }
}