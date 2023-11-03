package ru.fabit.udf.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

open class EventsStore<State : EventsState<Event>, Action, Event>(
    startState: State,
    reducer: Reducer<State, Action>,
    errorHandler: ErrorHandler,
    bootstrapAction: Action? = null,
    sideEffects: Iterable<SideEffect<State, Action>> = emptyList(),
    bindActionSources: Iterable<BindActionSource<State, Action>> = emptyList(),
    actionSources: Iterable<ru.fabit.udf.store.ActionSource<Action>> = emptyList(),
    actionHandlers: Iterable<ru.fabit.udf.store.ActionHandler<State, Action>> = emptyList()
) : ru.fabit.udf.store.BaseStore<State, Action>(
    startState,
    reducer,
    errorHandler,
    bootstrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
) {
    @Suppress("UNCHECKED_CAST")
    override val state: Flow<State>
        get() {
            _state.tryEmit(currentState)
            return _state.onEach {
                _currentState = currentState.clearEvents() as State
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun reduceState(state: State, action: Action): State {
        return if (_state.subscriptionCount.value == 0)
            super.reduceState(state, action)
        else
            super.reduceState(state.clearEvents() as State, action)
    }
}