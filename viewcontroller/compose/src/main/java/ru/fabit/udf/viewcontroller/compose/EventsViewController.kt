package ru.fabit.udf.viewcontroller.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ru.fabit.udf.store.EventsStore
import kotlin.coroutines.CoroutineContext

abstract class EventsViewController<State, Action, Event>(
    store: EventsStore<State, Action, Event>
) : ViewController<State, Action>(store) {
    private val event: Flow<List<Event>> = store.event

    @Composable
    fun renderEvent(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        context: CoroutineContext = Dispatchers.IO
    ): androidx.compose.runtime.State<List<Event>> = event.collectAsStateWithLifecycle(
        initialValue = listOf(),
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = minActiveState,
        context = context
    )
}