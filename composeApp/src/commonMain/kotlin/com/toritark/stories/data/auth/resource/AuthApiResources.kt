package com.toritark.stories.data.auth.resource

import com.toritark.stories.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*

@Resource("users/")
internal class AuthApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("auth/")
    class Auth(val parent: AuthApiResources = AuthApiResources()) {

        @Resource("anonymous/")
        class Anonymous(val parent: Auth = Auth())
    }
}