package com.toritark.stories.data.core_api.jwt.resource

import com.toritark.stories.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*

@Resource("users/auth/jwt/")
internal class JwtApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("token/")
    class Token(val parent: JwtApiResources = JwtApiResources()) {

        @Resource("refresh/")
        class Refresh(val parent: Token = Token())
    }
}