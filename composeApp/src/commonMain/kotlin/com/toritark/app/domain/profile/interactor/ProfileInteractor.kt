@file:OptIn(ExperimentalCoroutinesApi::class)

package com.toritark.app.domain.profile.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.profile.api.repository.ProfileApiRepository
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.data.profile.repository.ProfileRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface ProfileInteractor {
    val profileState: StateFlow<ProfileState>

    fun updateProfile(): Flow<Unit>
    fun updateProfileInBackground()
}

internal class ProfileInteractorImpl(
    private val profileApiRepository: ProfileApiRepository,
    private val profileRepository: ProfileRepository,
    defaultDispatcher: CoroutineDispatcher,
) : ProfileInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    override val profileState = profileRepository.profileState

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    override fun updateProfile(): Flow<Unit> {
        logger.d { "updateProfile" }

        return profileApiRepository
            .getProfile()
            .map { profile ->
                profileRepository.setProfile(profile)
            }
    }

    override fun updateProfileInBackground() {
        logger.d { "updateProfileInBackground" }

        coroutineScope.launch {
            updateProfile()
                .catch { t ->
                    logger.w(t) { "updateProfileInBackground: Failed to update profile" }
                }
                .collect {
                    logger.d { "updateProfileInBackground: done" }
                }
        }
    }

    private companion object {
        private const val LOG_TAG = "ProfileInteractor"
    }
}