package space.compoze.hiero.domain.settings.interactor

import space.compoze.hiero.domain.settings.repository.SettingsRepository

class SettingsGetTheme(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke() = settingsRepository.getTheme()

}