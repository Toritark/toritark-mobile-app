package com.toritark.app.presentation.core_ui.nav

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.toritark.app.util.core.uriDecode
import com.toritark.app.util.core.uriEncode
import kotlinx.serialization.json.Json

inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = true,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun get(bundle: SavedState, key: String): T? {
        bundle.read {
            return getStringOrNull(key)?.let(json::decodeFromString)
        }
    }

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write {
            putString(key, json.encodeToString(value))
        }
    }

    override fun parseValue(value: String): T = json.decodeFromString(uriDecode(value))

    override fun serializeAsValue(value: T): String = uriEncode(json.encodeToString(value))

}