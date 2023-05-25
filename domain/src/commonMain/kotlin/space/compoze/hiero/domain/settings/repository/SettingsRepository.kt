package space.compoze.hiero.domain.settings.repository

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import space.compoze.hiero.domain.base.exceptions.DomainError

interface SettingsRepository {
    fun getThemes(): Either<DomainError, List<String>>
    suspend fun getTheme(): Either<DomainError, String>
    fun flowTheme(): Either<DomainError, Flow<String>>
    suspend fun setTheme(theme: String): Either<DomainError, Unit>
}