package ru.fabit.udf.example.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.fabit.udf.example.data.DataRepositoryImpl
import ru.fabit.udf.example.data.ErrorHandlerImpl
import ru.fabit.udf.example.data.FeatureRepositoryImpl
import ru.fabit.udf.example.domain.repo.DataRepository
import ru.fabit.udf.example.domain.repo.FeatureRepository
import ru.fabit.udf.store.coroutines.ErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {
    @Binds
    @Singleton
    internal abstract fun bindDataRepository(impl: DataRepositoryImpl): DataRepository

    @Binds
    @Singleton
    internal abstract fun bindFeatureRepository(impl: FeatureRepositoryImpl): FeatureRepository

    @Binds
    @Singleton
    internal abstract fun bindErrorHandler(impl: ErrorHandlerImpl): ErrorHandler
}