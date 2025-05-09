package com.toritark.stories.data.language.resources

import com.toritark.stories.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*

@Resource("languages/")
internal class LanguagesApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("")
    class List(val parent: LanguagesApiResources = LanguagesApiResources())
}