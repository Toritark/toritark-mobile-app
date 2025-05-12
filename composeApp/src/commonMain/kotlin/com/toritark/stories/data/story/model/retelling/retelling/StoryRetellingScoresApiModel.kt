package com.toritark.stories.data.story.model.retelling.retelling

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryRetellingScoresApiModel(
    @SerialName("overall")
    val overall: Int,
    @SerialName("completeness")
    val completeness: Int,
    @SerialName("grammar")
    val grammar: Int,
    @SerialName("vocabulary")
    val vocabulary: Int,
    @SerialName("spelling")
    val spelling: Int,
    @SerialName("punctuation")
    val punctuation: Int,
)
