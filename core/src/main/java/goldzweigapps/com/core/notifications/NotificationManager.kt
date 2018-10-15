package goldzweigapps.com.core.notifications

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.app.NotificationManager as AppNotificationMangaer

/**
 * Created by gilgoldzweig on 13/09/2017.
 */
class NotificationManager(val context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as AppNotificationMangaer
    val notificationChannels = ArrayList<NotificationChannel>()
    val notificationChannelGroups = ArrayList<NotificationChannelGroup>()
    val notifications = ArrayList<Pair<Int, Notification>>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun channel(id: String = NotificationChannel.DEFAULT_CHANNEL_ID,
                name: String = "default channel name",
                importance: Int = AppNotificationMangaer.IMPORTANCE_DEFAULT,
                channel: NotificationChannel.() -> Unit): NotificationChannel {
        val notificationChannel = NotificationChannel(id, name, importance)
                .apply(channel)
        notificationChannels + notificationChannel
        notificationManager.createNotificationChannels(notificationChannels)
        return notificationChannel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    inline fun channelGroup(id: String = NotificationChannel.DEFAULT_CHANNEL_ID,
                            name: String = "default channel name",
                            channel: NotificationChannelGroup.() -> Unit): NotificationChannelGroup {
        val notificationsChannelGroup = NotificationChannelGroup(id, name)
                .apply(channel)
        notificationChannelGroups + notificationsChannelGroup
        notificationManager.createNotificationChannelGroups(notificationChannelGroups)
        return notificationsChannelGroup
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun showSingle(notificationId: Int, notification: Notification) {
        notificationManager.notify(notificationId, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun showAll() {
        notifications.forEach { showSingle(it.first, it.second) }
    }
}

inline fun Context.notificationManager(manager: NotificationManager.() -> Unit) =
        NotificationManager(this)
                .apply(manager)
