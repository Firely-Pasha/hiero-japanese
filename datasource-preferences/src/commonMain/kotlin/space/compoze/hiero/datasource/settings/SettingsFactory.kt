package space.compoze.hiero.datasource.settings

import com.russhwolf.settings.Settings

expect class SettingsFactory {
    fun create(): Settings
}
