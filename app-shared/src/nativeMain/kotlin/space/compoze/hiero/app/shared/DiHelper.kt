package space.compoze.hiero.app.shared

import org.koin.core.context.startKoin

fun initDi() {
    startKoin {
        modules(appModule())
    }
}

class KEK() {
    fun LOL() {

    }
}