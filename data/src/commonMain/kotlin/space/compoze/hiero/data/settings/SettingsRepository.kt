package space.compoze.hiero.data.settings

import arrow.core.raise.catch
import arrow.core.raise.either
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.flow
import space.compoze.hiero.domain.base.exceptions.DomainError
import space.compoze.hiero.domain.settings.enums.AppSettings
import space.compoze.hiero.domain.settings.repository.SettingsRepository

class SettingsRepository(
    private val settings: FlowSettings,
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

    override suspend fun getTheme() = either {
        catch({
            settings.getStringOrNull(AppSettings.Theme._KEY)
                ?: AppSettings.Theme.SYSTEM
        }) {
            raise(DomainError("SettingsRepository::getTheme error", it))
        }
    }

    override fun flowTheme() = either {
        catch({
            settings.getStringFlow(AppSettings.Theme._KEY, AppSettings.Theme.SYSTEM)
        }) {
            raise(DomainError("SettingsRepository::flowTheme error", it))
        }
    }

    override suspend fun setTheme(theme: String) = either {
        catch({
            settings.putString(AppSettings.Theme._KEY, theme)
        }) {
            raise(DomainError("SettingsRepository::setTheme(theme: $theme) error", it))
        }
    }
}