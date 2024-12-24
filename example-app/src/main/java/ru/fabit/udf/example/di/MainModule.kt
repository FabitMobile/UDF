package ru.fabit.udf.example.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.fabit.udf.example.presentation.store.MainStore
import ru.fabit.udf.example.presentation.store.actionhandler.ButtonClickActionHandler
import ru.fabit.udf.example.presentation.store.actionsource.GetDataActionSource
import ru.fabit.udf.example.presentation.store.bindactionsource.OverflowBindActionSource
import ru.fabit.udf.example.presentation.store.reducer.MainReducer
import ru.fabit.udf.example.presentation.store.sideeffect.FeatureToggleSideEffect
import ru.fabit.udf.example.presentation.store.sideeffect.GetFeatureStateSideEffect
import ru.fabit.udf.example.presentation.store.state.MainState
import ru.fabit.udf.store.coroutines.ErrorHandler
import ru.fabit.udf.store.coroutines.StoreKit

@Module
@InstallIn(ViewModelComponent::class)
class MainModule {
    @Provides
    @ViewModelScoped
    internal fun provideMainStore(
        errorHandler: ErrorHandler,
        getDataActionSource: GetDataActionSource,
        buttonClickActionHandler: ButtonClickActionHandler,
        featureToggleSideEffect: FeatureToggleSideEffect,
        getFeatureStateSideEffect: GetFeatureStateSideEffect,
        overflowBindActionSource: OverflowBindActionSource
    ) = MainStore(
        StoreKit(
            startState = MainState(),
            reducer = MainReducer,
            errorHandler = errorHandler,
            actionSources = listOf(getDataActionSource),
            actionHandlers = listOf(buttonClickActionHandler),
            bindActionSources = listOf(overflowBindActionSource),
            sideEffects = listOf(
                featureToggleSideEffect,
                getFeatureStateSideEffect
            )
        )
    )
}