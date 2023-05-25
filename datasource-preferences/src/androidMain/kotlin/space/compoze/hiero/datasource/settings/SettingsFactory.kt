package space.compoze.hiero.datasource.settings

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings

actual class SettingsFactory(
    private val context: Context
) {
    actual fun create(): FlowSettings {
        return SharedPreferencesSettings(context.getSharedPreferences("app_settings", Context.MODE_PRIVATE))
            .toFlowSettings()
    }
}