package space.compoze.hiero.data.settings

import arrow.core.raise.catch
import arrow.core.raise.either
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.settings.enums.AppSettings
import space.compoze.hiero.domain.settings.repository.SettingsRepository

class SettingsRepository(
    private val settings: Settings,
) : SettingsRepository {

    override fun getThemes() = either {
        catch({
            listOf(
                AppSettings.Theme.SYSTEM,
            )
        }) {
            raise(DomainError("SettingsRepository::getTheme error", it))
        }
    }

    override fun getTheme() = either {
        catch({
            settings.getStringOrNull(AppSettings.Theme._KEY)
                ?: AppSettings.Theme.SYSTEM
        }) {
            raise(DomainError("SettingsRepository::getTheme error", it))
        }
    }

    override fun setTheme(theme: String) = either {
        catch({
            settings[AppSettings.Theme._KEY] = theme
        }) {
            raise(DomainError("SettingsRepository::setTheme(theme: $theme) error", it))
        }
    }
}