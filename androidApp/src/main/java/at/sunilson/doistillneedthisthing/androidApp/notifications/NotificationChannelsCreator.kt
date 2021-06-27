package at.sunilson.doistillneedthisthing.androidApp.notifications

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import at.sunilson.doistillneedthisthing.androidApp.R

internal const val DECISIONS_NOTIFICATION_GROUP = "remindersGroup"
internal const val GENERAL_NOTIFICATION_GROUP = "generalGroup"
internal const val WEEKLY_DECISIONS_NOTIFICATION_CHANNEL = "weeklyReminders"
internal const val UPDATES_NOTIFICATION_CHANNEL = "updates"
internal const val DAILY_RANDOM_DECISIONS_NOTIFICATION_CHANNEL = "dailyReminders"

/**
 * Creates all notification channels used by this application
 */
@RequiresApi(Build.VERSION_CODES.O)
internal fun createNotificationChannels(context: Context) {
    val manager = NotificationManagerCompat.from(context)

    val groups = listOf(
        NotificationChannelGroup(
            DECISIONS_NOTIFICATION_GROUP,
            context.getString(R.string.notification_group_decisions)
        ),
        NotificationChannelGroup(
            GENERAL_NOTIFICATION_GROUP,
            context.getString(R.string.notification_group_general)
        )
    )
    groups.forEach { manager.createNotificationChannelGroup(it) }

    val channels = listOf(
        NotificationChannel(
            WEEKLY_DECISIONS_NOTIFICATION_CHANNEL,
            context.getString(R.string.notification_channel_weekly_decisions_title),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(true)
            group = DECISIONS_NOTIFICATION_GROUP
            description =
                context.getString(R.string.notification_channel_weekly_decisions_description)
        },
        NotificationChannel(
            UPDATES_NOTIFICATION_CHANNEL,
            context.getString(R.string.notification_channel_updates_title),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(true)
            group = GENERAL_NOTIFICATION_GROUP
            description = context.getString(R.string.notification_channel_updates_description)
        },
        NotificationChannel(
            DAILY_RANDOM_DECISIONS_NOTIFICATION_CHANNEL,
            context.getString(R.string.notification_channel_daily_decisions_title),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(true)
            group = DECISIONS_NOTIFICATION_GROUP
            description =
                context.getString(R.string.notification_channel_daily_decisions_description)
        },
    )
    channels.forEach { manager.createNotificationChannel(it) }
}