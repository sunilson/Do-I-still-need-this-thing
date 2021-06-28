package at.sunilson.doistillneedthisthing.androidApp.notifications

import android.annotation.SuppressLint
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Starts all periodic workers that the app uses
 */
@SuppressLint("UnsafeOptInUsageError")
internal fun createPeriodicWorkers(workManager: WorkManager) {
    workManager.enqueueUniquePeriodicWork(
        "weeklyDecisions",
        ExistingPeriodicWorkPolicy.KEEP,
        PeriodicWorkRequestBuilder<WeeklyDecisionsWorker>(1L, TimeUnit.DAYS)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    )
    workManager.enqueueUniquePeriodicWork(
        "randomSingleDecisions",
        ExistingPeriodicWorkPolicy.KEEP,
        PeriodicWorkRequestBuilder<RandomDecisionWorker>(1L, TimeUnit.HOURS)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    )
}