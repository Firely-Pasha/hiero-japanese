package space.compoze.hiero.domain.settings.repository

import arrow.core.Either
import space.compoze.hiero.domain.base.exceptions.DomainError

interface SettingsRepository {
    fun getThemes(): Either<DomainError, List<String>>
    fun getTheme(): Either<DomainError, String>
    fun setTheme(theme: String): Either<DomainError, Unit>
}