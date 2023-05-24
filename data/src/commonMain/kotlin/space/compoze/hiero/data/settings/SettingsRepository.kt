package space.compoze.hiero.data.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import space.compoze.hiero.domain.settings.enums.AppSettings
import space.compoze.hiero.domain.settings.repository.SettingsRepository

class SettingsRepository(
    private val settings: Settings
) : SettingsRepository {

    override fun getThemes(): List<String> {
        return listOf(
            AppSettings.Theme.SYSTEM,
        )
    }

    override fun getTheme(): String {
        return settings.getStringOrNull(AppSettings.Theme._KEY)
            ?: AppSettings.Theme.SYSTEM
    }

    override fun setTheme(theme: String) {
        settings[AppSettings.Theme._KEY] = theme
    }
}