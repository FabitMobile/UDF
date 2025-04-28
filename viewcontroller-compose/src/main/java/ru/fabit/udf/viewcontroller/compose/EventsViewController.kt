package ru.fabit.udf.viewcontroller.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.EventsStore
import ru.fabit.udf.viewcontroller.compose.internal.log
import kotlin.coroutines.CoroutineContext

abstract class EventsViewController<State, Action, Event>(
    private val eventsStore: EventsStore<State, Action, Event>
) : ViewController<State, Action>(eventsStore) {

    @Composable
    fun renderEvents(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        context: CoroutineContext = Dispatchers.Main
    ): androidx.compose.runtime.State<List<Event>> {
        log("EventsViewController renderEvents")
        val mutableEventsState = remember {
            mutableStateListOf<Event>()
        }
        val eventsState = rememberUpdatedState(newValue = mutableEventsState.toList())
        SideEffect {
            if (mutableEventsState.isNotEmpty()) {
                mutableEventsState.clear()
            }
        }
        LaunchedEffect("EventsViewController") {
            launch(context) {
                eventsStore.eventsFlow
                    .flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
                    .collect {
                        log("LaunchedEffect(EventsViewController) collect events = $it")
                        mutableEventsState.addAll(it)
                    }
            }
        }
        return eventsState
    }
}