package com.toritark.app.data.ads.api.resource

import com.toritark.app.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*

@Resource("ads/")
internal class AdsApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("bonuses/")
    class Bonuses(val parent: AdsApiResources = AdsApiResources()) {
        
        @Resource("not-consumed/")
        class NotConsumed(val parent: Bonuses = Bonuses()) {

            @Resource("count/")
            class Count(val parent: NotConsumed = NotConsumed())
        }
    }
}