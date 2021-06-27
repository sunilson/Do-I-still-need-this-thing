package at.sunilson.doistillneedthisthing.androidApp

import android.app.Application
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import at.sunilson.doistillneedthisthing.androidApp.notifications.createNotificationChannels
import at.sunilson.doistillneedthisthing.androidApp.notifications.createPeriodicWorkers
import at.sunilson.doistillneedthisthing.shared.data.appContext
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(Timber.DebugTree())
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels(this)
        }
        createPeriodicWorkers(WorkManager.getInstance(this))
    }
}