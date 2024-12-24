package ru.fabit.udf.example.domain.usecase

import ru.fabit.udf.example.domain.repo.FeatureRepository
import javax.inject.Inject

class IsFeatureEnabledUseCase @Inject constructor(
    private val featureRepository: FeatureRepository
) {
    suspend fun build() = featureRepository.isFeatureEnabled()
}