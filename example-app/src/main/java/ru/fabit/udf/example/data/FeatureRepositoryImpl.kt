package ru.fabit.udf.example.data

import android.content.Context
import android.content.Context.*
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.fabit.udf.example.domain.repo.FeatureRepository
import javax.inject.Inject

class FeatureRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : FeatureRepository {
    private val pref = context.getSharedPreferences("example", MODE_PRIVATE)

    override suspend fun isFeatureEnabled(): Boolean {
        return pref.getBoolean("feature", false)
    }

    override suspend fun setFeatureEnabled(isEnabled: Boolean) {
        pref.edit {
            putBoolean("feature", isEnabled)
        }
    }
}