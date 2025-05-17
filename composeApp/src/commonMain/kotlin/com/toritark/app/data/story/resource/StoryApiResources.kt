package com.toritark.app.data.story.resource

import com.toritark.app.data.core_api.base.resource.BaseApiResource
import io.ktor.resources.*
import kotlinx.serialization.SerialName

@Resource("stories/")
internal class StoryApiResources(val parent: BaseApiResource = BaseApiResource()) {

    @Resource("story-requests/")
    class StoryRequests(val parent: StoryApiResources = StoryApiResources()) {

        @Resource("")
        class List(val parent: StoryRequests = StoryRequests())

        @Resource("")
        class Create(val parent: StoryRequests = StoryRequests())

        @Resource("{id}/")
        class Get(
            val parent: StoryRequests = StoryRequests(),
            @SerialName("id")
            val id: Long,
        )
    }

    @Resource("story-retellings-reviews/")
    class StoryRetellingsReviews(val parent: StoryApiResources = StoryApiResources()) {

        @Resource("")
        class List(val parent: StoryRetellingsReviews = StoryRetellingsReviews())

        @Resource("")
        class Create(val parent: StoryRetellingsReviews = StoryRetellingsReviews())

        @Resource("{id}/")
        class Get(
            val parent: StoryRetellingsReviews = StoryRetellingsReviews(),
            @SerialName("id")
            val id: Long,
        )
    }
}