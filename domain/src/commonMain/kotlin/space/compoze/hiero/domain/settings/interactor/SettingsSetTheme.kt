package space.compoze.hiero.domain.settings.interactor

import space.compoze.hiero.domain.settings.repository.SettingsRepository

class SettingsSetTheme(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(theme: String) = settingsRepository.setTheme(theme)

}