package ru.fabit.udf.example.domain.usecase

import ru.fabit.udf.example.domain.repo.FeatureRepository
import javax.inject.Inject

class SetFeatureEnabledUseCase @Inject constructor(
    private val featureRepository: FeatureRepository
) {
    suspend fun build(isEnabled: Boolean) = featureRepository.setFeatureEnabled(isEnabled)
}