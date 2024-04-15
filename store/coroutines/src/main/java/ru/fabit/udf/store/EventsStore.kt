package ru.fabit.udf.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach

open class EventsStore<State, Action, Event>(
    startState: State,
    private val eventsReducer: EventsReducer<State, Action, Event>,
    errorHandler: ErrorHandler,
    bootstrapAction: Action? = null,
    sideEffects: Iterable<SideEffect<State, Action>> = emptyList(),
    bindActionSources: Iterable<BindActionSource<State, Action>> = emptyList(),
    actionSources: Iterable<ActionSource<Action>> = emptyList(),
    actionHandlers: Iterable<ActionHandler<State, Action>> = emptyList()
) : BaseStore<State, Action>(
    startState,
    eventsReducer,
    errorHandler,
    bootstrapAction,
    sideEffects,
    bindActionSources,
    actionSources,
    actionHandlers
) {

    protected val _event = MutableSharedFlow<List<Event>>(replay = 1)
    private var events: List<Event> = mutableListOf()

    val event: Flow<List<Event>>
        get() {
            return _event.onEach {
                if (it.isNotEmpty()) {
                    //                    println("___TEST___ list =$it clear ")
                    _event.tryEmit(listOf())
                    events = listOf()
                }
            }
        }

    override suspend fun reduceState(state: State, action: Action): State {
        val wrapState = eventsReducer.reduceEvent(state, action)
        val newState = wrapState.state
        val newEvents = wrapState.events
        val newList = events + newEvents

        events = newList
        _event.tryEmit(newList)
        //        println("___TEST___ newList = $newList")
        return newState
    }
}