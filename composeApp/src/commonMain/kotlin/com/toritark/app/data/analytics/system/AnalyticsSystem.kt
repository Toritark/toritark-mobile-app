package com.toritark.app.data.analytics.system

import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.analytics.model.AnalyticsProperty

abstract class AnalyticsSystem(
    protected val name: String,
) {
    protected val logger = Logger.withTag("AnalyticsSystem-$name")

    // Old name -> new name
    protected open val eventsToRename = mapOf<String, String>()

    // Event name (or `*` for all events) -> <old parameter name -> new parameter name>
    protected open val eventParametersToRename = mapOf<String, Map<String, String>>()

    // Old name -> new name
    protected open val propertiesToRename = mapOf<String, String>()

    open fun initialize() {
        logger.d { "initialize" }
    }

    fun setProperty(property: AnalyticsProperty) {
        renamePropertyIfNeeded(property).let(::setPropertyInternal)
    }

    private fun renamePropertyIfNeeded(property: AnalyticsProperty): AnalyticsProperty {
        val newName = propertiesToRename[property.name] ?: return property
        return property.copy(name = newName)
    }

    protected abstract fun setPropertyInternal(property: AnalyticsProperty)

    fun logEvent(event: AnalyticsEvent) {
        renameEventAndParametersIfNeeded(event).let(::logEventInternal)
    }

    private fun renameEventAndParametersIfNeeded(event: AnalyticsEvent): AnalyticsEvent {
        val newEventName = eventsToRename[event.name]

        // Get parameter renaming maps that apply to this event
        val wildcardParameterRenames = eventParametersToRename["*"]
        val eventSpecificParameterRenames = eventParametersToRename[event.name]

        // If no renaming is needed, return the original event
        if (newEventName == null && wildcardParameterRenames == null && eventSpecificParameterRenames == null) {
            return event
        }

        // Create a new parameters map if needed
        val newParameters = if (wildcardParameterRenames != null || eventSpecificParameterRenames != null) {
            event.parameters.entries.associate { (paramName, paramValue) ->
                val newParamName = eventSpecificParameterRenames?.get(paramName)
                    ?: wildcardParameterRenames?.get(paramName)
                    ?: paramName
                newParamName to paramValue
            }
        } else {
            event.parameters
        }

        // Create a new event with the renamed name and/or parameters
        return event.copy(
            name = newEventName ?: event.name,
            parameters = newParameters
        )
    }

    abstract fun logEventInternal(event: AnalyticsEvent)

    fun logScreenView(screenName: String) {
        logger.d { "logScreenView: $screenName" }

        logScreenViewInternal(screenName)
    }

    protected open fun logScreenViewInternal(screenName: String) {}
}
