package ru.fabit.viewcontroller.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.fabit.udf.store.coroutines.Store

abstract class ViewController<State, Action>(
    protected val store: Store<State, Action>,
    private val savedStateHandle: SavedStateHandle? = null
) : ViewModel(), LifecycleEventObserver {
    private var isFirstAttach = true
    protected var isAttach = false

    private var view: StateView<State>? = null

    private var subscription: Job? = null

    init {
        store.start()
    }

    protected open fun onResume() {}

    protected open fun onPause() {}

    protected open fun firstViewAttach() {}

    fun setArguments(mapArgument: Map<String, Any?>) {
        mapArgument.forEach { entry ->
            savedStateHandle?.set(entry.key, entry.value)
        }
    }

    protected fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                isAttach = true
                view = source as StateView<State>
                subscription = viewModelScope.launch {
                    store.state.collect {
                        view?.renderState(it)
                    }
                }
                onResume()
                if (isFirstAttach) {
                    isFirstAttach = false
                    firstViewAttach()
                }
            }

            Lifecycle.Event.ON_PAUSE -> {
                onPause()
                isAttach = false
                subscription?.cancel()
            }

            else -> {}
        }
    }

    override fun onCleared() {
        store.dispose()
    }
}