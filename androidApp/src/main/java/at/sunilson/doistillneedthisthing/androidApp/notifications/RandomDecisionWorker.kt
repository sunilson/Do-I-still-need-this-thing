package at.sunilson.doistillneedthisthing.androidApp.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.content.edit
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import at.sunilson.doistillneedthisthing.androidApp.R
import at.sunilson.doistillneedthisthing.androidApp.notifications.DecisionsBroadcastReceiver.Companion.ACTION_NEEDED
import at.sunilson.doistillneedthisthing.androidApp.notifications.DecisionsBroadcastReceiver.Companion.ACTION_NOT_NEEDED
import at.sunilson.doistillneedthisthing.androidApp.notifications.DecisionsBroadcastReceiver.Companion.ITEM_ID
import at.sunilson.doistillneedthisthing.androidApp.notifications.DecisionsBroadcastReceiver.Companion.NOTIFICATION_ID
import at.sunilson.doistillneedthisthing.androidApp.util.BitmapUtils
import at.sunilson.doistillneedthisthing.shared.domain.GetRandomDecision
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.extensions.imageUri
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import kotlin.random.Random

@HiltWorker
class RandomDecisionWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val getRandomDecision: GetRandomDecision,
    private val sharedPreferences: SharedPreferences
) : CoroutineWorker(context, workerParameters) {

    private val notificationManager
        get() = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager;

    private val componentName = ComponentName(
        "at.sunilson.doistillneedthisthing.androidApp",
        "at.sunilson.doistillneedthisthing.androidApp.notifications.DecisionsBroadcastReceiver"
    )

    private fun generateIntent(notificationId: Int, decisionId: Long, action: String): Intent {
        return Intent().apply {
            component = componentName
            this.action = action
            putExtra(NOTIFICATION_ID, notificationId)
            putExtra(ITEM_ID, decisionId)
        }
    }

    private fun makePendingIntent(intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(context, 1, intent, FLAG_UPDATE_CURRENT)
    }

    override suspend fun doWork(): Result {
        Timber.d("Started random decision worker!")
        val (randomDecision, error) = getRandomDecision(Unit)

        if (error != null) {
            Timber.e(error, "Could not load random decision!")
            return Result.failure(
                Data.Builder().putString("error", error.message.orEmpty()).build()
            )
        }

        if (randomDecision == null) {
            Timber.d("Random decision was null!")
            return Result.failure(
                Data.Builder().putString("error", "Random decision was null!").build()
            )
        }

        // Generate notification id and check if previous one still exists
        val notificationId = Random.nextInt()
        val lastId = sharedPreferences.getInt(LATEST_NOTIFICATION_ID, -1)
        if (notificationManager.activeNotifications.firstOrNull { it.id == lastId } != null) {
            return Result.failure(
                Data.Builder().putString("error", "There was already a notification shown").build()
            )
        }
        sharedPreferences.edit { putInt(LATEST_NOTIFICATION_ID, notificationId) }

        // Notifiy and return success
        notificationManager.notify(
            notificationId,
            createNotification(notificationId, randomDecision)
        )
        Timber.d("Shown notification for item: $randomDecision")
        return Result.success()
    }

    private suspend fun createNotification(notificationId: Int, item: Item): Notification {
        // Create intents
        val neededIntent = generateIntent(notificationId, item.id, ACTION_NEEDED)
        val notNeededIntent = generateIntent(notificationId, item.id, ACTION_NOT_NEEDED)

        // Create notification
        val image = BitmapUtils.loadBitmapFromUri(context, item.imageUri)
        return NotificationCompat.Builder(context, DAILY_RANDOM_DECISIONS_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_baseline_camera_24)
            .setContentTitle("Brauchst du das noch?")
            .setContentText(item.name)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
            .setPriority(PRIORITY_HIGH)
            .addAction(R.drawable.ic_baseline_camera_24, "Ja", makePendingIntent(neededIntent))
            .addAction(R.drawable.ic_baseline_camera_24, "Nein", makePendingIntent(notNeededIntent))
            .setPriority(PRIORITY_DEFAULT)
            .build()
    }

    companion object {
        private const val LATEST_NOTIFICATION_ID = "latestNotificationId"
    }
}