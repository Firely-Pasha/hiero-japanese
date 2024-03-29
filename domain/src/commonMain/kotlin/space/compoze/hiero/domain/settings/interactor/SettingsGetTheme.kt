package space.compoze.hiero.domain.settings.interactor

import space.compoze.hiero.domain.settings.repository.SettingsRepository

class SettingsGetTheme(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke() = settingsRepository.getTheme()

    fun asFlow() = settingsRepository.flowTheme()

}