package goldzweigapps.com.library.notifications

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager as AppNotificationMangaer
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi

/**
 * Created by gilgoldzweig on 13/09/2017.
 */
class NotificationManager(val context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as AppNotificationMangaer
    val notificationChannels = ArrayList<NotificationChannel>()
    val notificationChannelGroups = ArrayList<NotificationChannelGroup>()
    val notifications = ArrayList<Pair<Int, Notification>>()

    inline fun notification(notificationId: Int, channelId: String, notificationFunc: Notification.() -> Unit): Notification {
        val notification = Notification(context, channelId).apply(notificationFunc)
        notifications + notificationId to notification
        return notification
    }

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
