package ru.fabit.udf.example.presentation.store

import ru.fabit.udf.example.presentation.store.action.MainAction
import ru.fabit.udf.example.presentation.store.state.MainEvent
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.EventsStore
import ru.fabit.udf.store.coroutines.StoreKit

class MainStore(
    storeKit: StoreKit<MainState, MainAction>
) : EventsStore<MainState, MainAction, MainEvent>(storeKit)