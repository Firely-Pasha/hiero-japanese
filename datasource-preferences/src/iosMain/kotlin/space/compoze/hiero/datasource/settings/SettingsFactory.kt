package space.compoze.hiero.datasource.settings

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import platform.Foundation.NSUserDefaults

actual class SettingsFactory {
    actual fun create(): FlowSettings {
        return NSUserDefaultsSettings(NSUserDefaults()).toFlowSettings()
    }
}