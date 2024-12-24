package ru.fabit.udf.example.domain.usecase

import ru.fabit.udf.example.domain.repo.DataRepository
import javax.inject.Inject

class IncreaseCounterUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    suspend fun build() = dataRepository.increaseCounter()
}