package ru.fabit.udf.example.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import ru.fabit.udf.example.domain.entity.Data
import ru.fabit.udf.example.domain.repo.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor() : DataRepository {
    private val flow = MutableStateFlow(Data(0))

    override suspend fun getData(): Flow<Data> {
        return flow {
            var count = 0
            flow.collect { data ->
                if (count > 0)
                    delay(500)
                emit(data)
                count++
            }
        }
    }

    override suspend fun increaseCounter() {
        val old = flow.value
        flow.value = old.copy(counter = old.counter + 1)
    }
}