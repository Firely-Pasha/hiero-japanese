package space.compoze.hiero.app.shared

import kotlinx.coroutines.Dispatchers
import space.compoze.hiero.domain.base.AppDispatchers

actual object PlatformDispatchers : AppDispatchers {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val unconfined = Dispatchers.Unconfined
}