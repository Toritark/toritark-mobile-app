package com.toritark.app.presentation.core_ui.nav

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.toritark.app.util.core.uriDecode
import com.toritark.app.util.core.uriEncode
import kotlinx.serialization.json.Json

inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = true,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(uriDecode(value))

    override fun serializeAsValue(value: T): String = uriEncode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, json.encodeToString(value))

}