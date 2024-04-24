package ru.fabit.udf.viewcontroller.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import ru.fabit.udf.store.Store
import kotlin.coroutines.CoroutineContext

abstract class ViewController<State, Action>(
    protected val store: Store<State, Action>
) : ViewModel() {

    init {
        store.start()
    }

    protected fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    override fun onCleared() {
        store.dispose()
    }

    @Composable
    fun renderState(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        context: CoroutineContext = Dispatchers.IO
    ): androidx.compose.runtime.State<State> = store.state.collectAsStateWithLifecycle(
        initialValue = store.currentState,
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = minActiveState,
        context = context
    )
}