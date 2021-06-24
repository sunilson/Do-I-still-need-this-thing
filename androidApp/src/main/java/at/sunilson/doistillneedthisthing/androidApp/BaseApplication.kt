package at.sunilson.doistillneedthisthing.androidApp

import android.app.Application
import at.sunilson.doistillneedthisthing.shared.data.appContext
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(Timber.DebugTree())
    }
}