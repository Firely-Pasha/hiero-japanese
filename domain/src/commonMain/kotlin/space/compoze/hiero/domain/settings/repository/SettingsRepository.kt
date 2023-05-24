package space.compoze.hiero.domain.settings.repository

interface SettingsRepository {
    fun getThemes(): List<String>
    fun getTheme(): String
    fun setTheme(theme: String)
}