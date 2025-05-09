package com.toritark.stories.domain.story.exception

sealed class CreateStoryException : Exception() {

    class CreateStoryConfigurationException : CreateStoryException()
}