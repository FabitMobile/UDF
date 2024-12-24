package ru.fabit.udf.example.domain.repo

interface FeatureRepository {
    suspend fun isFeatureEnabled(): Boolean

    suspend fun setFeatureEnabled(isEnabled: Boolean)
}