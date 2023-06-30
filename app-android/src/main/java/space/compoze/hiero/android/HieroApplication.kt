package space.compoze.hiero.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import space.compoze.hiero.app.shared.appModule
import space.compoze.hiero.app.shared.startHieroApp

class HieroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@HieroApplication)
            modules(appModule())
        }
        deleteDatabase("hiero-2.db")
        startHieroApp()
    }

}