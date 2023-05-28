package space.compoze.hiero.app.shared

import org.koin.core.context.startKoin

@Suppress("unused")
fun initApplication() {
    startKoin {
        modules(appModule())
    }
}