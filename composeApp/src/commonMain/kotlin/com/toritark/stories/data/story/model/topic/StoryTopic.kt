package com.toritark.stories.data.story.model.topic

sealed class StoryTopic(
    val prompt: String,
) {

    data object DailyRoutine : StoryTopic(
        prompt = "A story where a character describes their typical day from waking up to going to bed. " +
                "Include many details.",
    )

    data object StoreDialogue : StoryTopic(
        prompt = "A scenario where a character goes to a specific type of shop " +
                "to buy something. The story should include dialogue between the customer and the shopkeeper. " +
                "Focus on vocabulary related to items in that shop, numbers/prices, and phrases for asking, offering, " +
                "and purchasing.",
    )

    data object FavoriteAnimal : StoryTopic(
        prompt = "A character describes their favorite animal or their own pet. The story should include adjectives " +
                "to describe its appearance (color, size, features) and personality/behavior. " +
                "Also, include actions the animal does or that the character does with the animal. "
    )

    data object Walk : StoryTopic(
        prompt = "A story where a character walks through a scenic area. The story should include " +
                "descriptions of the area and the characters' interactions with it. The character faces an everyday problem. " +
                "The character should be able to explain their feelings about the area and the characters' interactions with it. ",
    )

    data object MeetingNewFriend : StoryTopic(
        prompt = "Two characters meet for the first time (e.g., at school, in a playground, at a neighbor's house or at ANY other place). " +
                "The story should include greetings, introductions (name, maybe age for kids' stories), " +
                "and perhaps one or two questions they ask each other.",
    )

    data object SpecialDay : StoryTopic(
        prompt = "A story where a character describes their special day. The story should include actions the character does (e.g., going to a party, " +
                "attending a concert, or going to a movie) and and feelings (happy, excited, etc).",
    )

    data object MyDream : StoryTopic(
        prompt = "A character describes a pleasant dream they had. This allows for a bit more imagination but " +
                "keep the vocabulary and sentence structure basic. It could involve flying, talking animals, or visiting " +
                "a magical place, etc, but keep the narrative linear.",
    )

    data object MyRoom : StoryTopic(
        prompt = "A character describes their room or a part of their house. Focus on using \"there is/there are,\" " +
                "prepositions of place (on, in, under, next to), and vocabulary for common furniture and household items.",
    )

    data object Family : StoryTopic(
        prompt = "A story where a character describes their family (e.g., their parents, grandparents, siblings, " +
                "other family members, pets). The story should include their hobbies and temper.",
    )

    data object Custom : StoryTopic(
        prompt = "",
    )

    companion object {
        val allTopics = listOf(
            DailyRoutine,
            StoreDialogue,
            FavoriteAnimal,
            Walk,
            MeetingNewFriend,
            SpecialDay,
            MyDream,
            MyRoom,
            Family,
            Custom,
        )
    }
}