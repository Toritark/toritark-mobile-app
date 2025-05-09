package com.toritark.stories.data.onboarding.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface OnboardingRepository {
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun setOnboardingCompleted()
}

internal class OnboardingRepositoryImpl(
    private val settings: Settings,
    private val ioDispatcher: CoroutineDispatcher,
) : OnboardingRepository {

    override suspend fun isOnboardingCompleted(): Boolean {
        return withContext(ioDispatcher) {
            settings.getBoolean(KEY_IS_ONBOARDING_COMPLETED, false)
        }
    }

    override suspend fun setOnboardingCompleted() {
        return withContext(ioDispatcher) {
            settings.putBoolean(KEY_IS_ONBOARDING_COMPLETED, true)
        }
    }

    private companion object {
        private const val KEY_IS_ONBOARDING_COMPLETED = "is_onboarding_completed"
    }
}