package at.sunilson.doistillneedthisthing.androidApp

import android.app.Application
import at.sunilson.doistillneedthisthing.androidApp.di.koinFactories
import at.sunilson.doistillneedthisthing.androidApp.di.koinSingles
import at.sunilson.doistillneedthisthing.androidApp.di.koinViewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(
                koinSingles,
                koinFactories,
                koinViewModels
            )
        }
    }
}