package com.example.adaptiveui.navigation

import androidx.navigation3.runtime.NavKey
import java.util.Base64
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed class Dest : NavKey {
    @Serializable
    data object Home : Dest()

    @Serializable
    data object Search : Dest()

    @Serializable
    data object Settings : Dest()

    @Serializable
    data class HomeDetail(val id: Int) : Dest()

    @Serializable
    data class SettingsDetail(val title: String, val desc: String) : Dest()

    fun encode(): String {
        val jsonString = json.encodeToString(serializer(), this)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(jsonString.toByteArray(Charsets.UTF_8))
    }

    companion object {
        private val json: Json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun decode(value: String): Dest {
            val jsonString = try {
                String(Base64.getUrlDecoder().decode(value), Charsets.UTF_8)
            } catch (_: IllegalArgumentException) {
                return when {
                    value == "home" -> Home
                    value == "search" -> Search
                    value == "settings" -> Settings
                    value.startsWith("homeDetail:") -> {
                        val id = value.removePrefix("homeDetail:").toIntOrNull() ?: -1
                        HomeDetail(id)
                    }

                    value.startsWith("settingsDetail:") -> {
                        val title = value.removePrefix("settingsDetail:")
                        SettingsDetail(title = title, desc = "")
                    }

                    else -> Home
                }
            }

            return try {
                json.decodeFromString(serializer(), jsonString)
            } catch (_: IllegalArgumentException) {
                Home
            }
        }
    }
}
