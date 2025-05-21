package com.toritark.app.data.billing.api.resource

import com.toritark.app.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*

@Resource("billing/")
internal class BillingApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("plans/")
    class Plans(val parent: BillingApiResources = BillingApiResources()) {

        @Resource("check/")
        class Check(val parent: Plans = Plans()) {

            @Resource("trigger/")
            class Trigger(val parent: Check = Check())
        }
    }
}