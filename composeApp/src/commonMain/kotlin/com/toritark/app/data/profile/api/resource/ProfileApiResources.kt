package com.toritark.app.data.profile.api.resource

import com.toritark.app.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*

@Resource("users/")
internal class ProfileApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("me/")
    class Me(val parent: ProfileApiResources = ProfileApiResources())
}