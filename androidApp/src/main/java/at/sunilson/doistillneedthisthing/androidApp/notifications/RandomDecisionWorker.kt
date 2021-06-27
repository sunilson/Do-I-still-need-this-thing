package at.sunilson.doistillneedthisthing.androidApp.notifications

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import at.sunilson.doistillneedthisthing.shared.domain.extensions.imageUri
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import kotlin.random.Random

@HiltWorker
class RandomDecisionWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val getRandomDecision: GetRandomDecision
) : CoroutineWorker(context, workerParameters) {

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

    private fun generatePendingIntent(intent: Intent): PendingIntent {
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

        // Create intents
        val notificationId = Random.nextInt()
        val neededIntent = generateIntent(notificationId, randomDecision.id, ACTION_NEEDED)
        val notNeededIntent = generateIntent(notificationId, randomDecision.id, ACTION_NOT_NEEDED)

        // Create notification
        val image = BitmapUtils.loadBitmapFromUri(context, randomDecision.imageUri)
        val notification =
            NotificationCompat.Builder(context, DAILY_RANDOM_DECISIONS_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_camera_24)
                .setContentTitle("Brauchst du das noch?")
                .setContentText(randomDecision.name)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(
                    R.drawable.ic_baseline_camera_24,
                    "Ja",
                    generatePendingIntent(neededIntent)
                )
                .addAction(
                    R.drawable.ic_baseline_camera_24,
                    "Nein",
                    generatePendingIntent(notNeededIntent)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notification)

        Timber.d("Shown notification for item: $randomDecision")
        return Result.success()
    }
}