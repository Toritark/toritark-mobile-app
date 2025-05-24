package com.toritark.app.domain.billing.model.plan

import com.toritark.app.data.billing.api.model.PlanApiModel

sealed interface PlanUpgradeCheckEvent {
    data class Waiting(
        val timeMs: Long,
    ) : PlanUpgradeCheckEvent

    data class Success(
        val timeMs: Long,
        val newPlan: PlanApiModel,
    ) : PlanUpgradeCheckEvent
}