package space.compoze.hiero.domain.base.exceptions

class DomainError(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Throwable(message, cause)