package com.toritark.app.domain.story.exception

sealed class CreateStoryException : Exception() {

    class CreateStoryConfigurationException : CreateStoryException()
}