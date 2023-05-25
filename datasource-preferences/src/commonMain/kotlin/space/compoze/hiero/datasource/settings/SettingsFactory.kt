package space.compoze.hiero.datasource.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings

expect class SettingsFactory {
    fun create(): FlowSettings
}
