package at.sunilson.doistillneedthisthing.androidApp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsChecked
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsNotNeeded
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
internal class DecisionsBroadcastReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    @Inject
    lateinit var markItemAsNotNeeded: MarkItemAsNotNeeded

    @Inject
    lateinit var markItemAsChecked: MarkItemAsChecked

    override fun onReceive(context: Context, intent: Intent) {
        val itemId = intent.getLongExtra(ITEM_ID, -1L)
        if (itemId == -1L) return

        val notificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
        if (notificationId == -1) return

        NotificationManagerCompat.from(context).cancel(notificationId)

        when (intent.action) {
            ACTION_NOT_NEEDED -> launch { markItemAsNotNeeded(itemId) }
            ACTION_NEEDED -> launch { markItemAsChecked(itemId) }
        }
    }

    companion object {
        const val ACTION_NOT_NEEDED = "notNeeded"
        const val ACTION_NEEDED = "needed"
        const val ITEM_ID = "itemId"
        const val NOTIFICATION_ID = "notificationId"
    }
}