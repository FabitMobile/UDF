package ru.fabit.udf.example.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.fabit.udf.example.domain.entity.Data

interface DataRepository {
    suspend fun getData(): Flow<Data>

    suspend fun increaseCounter()
}