@file:OptIn(ExperimentalCoroutinesApi::class)

package com.toritark.app.domain.profile.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.data.profile.api.repository.ProfileApiRepository
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.data.profile.model.ProfileSubscriptionState
import com.toritark.app.data.profile.repository.ProfileRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

interface ProfileInteractor {
    val profileState: StateFlow<ProfileState>
    val presentProfileState: Flow<ProfileState.Present>

    val subscriptionState: StateFlow<ProfileSubscriptionState>

    fun updateProfile(): Flow<Unit>
    fun updateProfileInBackground()

    suspend fun getCurrentPlan(): PlanApiModel
}

internal class ProfileInteractorImpl(
    private val profileApiRepository: ProfileApiRepository,
    private val profileRepository: ProfileRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) : ProfileInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    override val profileState = profileRepository.profileState

    override val presentProfileState: Flow<ProfileState.Present>
        get() {
            return profileState
                .filterIsInstance<ProfileState.Present>()
                .distinctUntilChanged()
                .flowOn(defaultDispatcher)
        }

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    override val subscriptionState: StateFlow<ProfileSubscriptionState> = profileState
        .map { profileState ->
            when (profileState) {
                ProfileState.Unknown, ProfileState.Missing -> ProfileSubscriptionState.Unknown
                is ProfileState.Present -> {
                    when (profileState.profile.plan.isFree) {
                        true -> ProfileSubscriptionState.Free
                        false -> ProfileSubscriptionState.Paid
                    }
                }
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileSubscriptionState.Unknown,
        )

    init {
        listenToSubscriptionState()
    }

    private fun listenToSubscriptionState() {
        coroutineScope.launch {
            subscriptionState
                .collect { state ->
                    logger.d { "listenToSubscriptionState: state=$state" }
                }
        }
    }

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

    override suspend fun getCurrentPlan(): PlanApiModel {
        return profileState
            .filterIsInstance<ProfileState.Present>()
            .map { it.profile.plan }
            .first()
    }

    private companion object {
        private const val LOG_TAG = "ProfileInteractor"
    }
}