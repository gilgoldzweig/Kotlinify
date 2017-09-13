package goldzweigapps.com.library.notifications

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import goldzweigapps.com.library.permissions.isApi23OrAbove
import goldzweigapps.com.library.permissions.isVersionAbove

/**
 * Created by gilgoldzweig on 04/09/2017.
 */
class Notification(private val context: Context,private  val channelId: String): NotificationCompat.Builder(context, channelId) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
    private val notificationChannels = ArrayList<NotificationChannel>()
    private val notificationChannelGroups = ArrayList<NotificationChannelGroup>()

    var contentTitle: String = ""
    set(value) {
        setContentTitle(value)
    }

    var contentText: String = ""
    set(value) {
        setContentText(value)
    }

    var smallIconRes: Int? = null
        @DrawableRes
        set(value) {
            if (value != null) setSmallIcon(value)
        }

    var largeIcon: Bitmap? = null
        set(value) {
            if (value != null) setLargeIcon(value)
        }

    var autoCancel: Boolean = true
        set(value) {
            setAutoCancel(value)
        }

    val actions = ArrayList<android.app.Notification.Action>()



    @RequiresApi(Build.VERSION_CODES.O)
    fun channel(id: String = NotificationChannel.DEFAULT_CHANNEL_ID,
                name: String = "default channel name",
                importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
                channel: NotificationChannel.() -> Unit): NotificationChannel {
        val notificationChannel = NotificationChannel(id, name, importance)
                .apply(channel)
        notificationChannels + notificationChannel
        setChannelId(id)
        return notificationChannel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun channelGroup(id: String = NotificationChannel.DEFAULT_CHANNEL_ID,
                     name: String = "default channel name",
                     channel: NotificationChannelGroup.() -> Unit): NotificationChannelGroup {
        val notificationsChannelGroup = NotificationChannelGroup(id, name)
                .apply(channel)
        notificationChannelGroups + notificationsChannelGroup
        return notificationsChannelGroup
    }

    @RequiresApi(Build.VERSION_CODES.N)
    inline fun action(init: Action.() -> Unit): android.app.Notification.Action {
        val action = Action().apply(init)
        val actionBuilder = with(action) {
           android.app.Notification.Action.Builder(icon, title, intent)
                   .setAllowGeneratedReplies(allowGeneratedReplies)
                   .addRemoteInput(remoteInput)
       }
        for (extra in action.extras) actionBuilder.addExtras(extra)
        val notificationAction = actionBuilder.build()
        actions + notificationAction
//        setActions(*actions.toTypedArray())
        return notificationAction
    }


    fun asNotification(): android.app.Notification = build()




    fun notify(id: Int) {
        notificationManager.notify(id, asNotification())
    }
}

inline fun Context.notification(id: String, notification: Notification.() -> Unit): Notification =
        Notification(this, id)
                .apply(notification)



class Action(var title: CharSequence = "",
             var icon: Icon? = null,
             var intent: PendingIntent? = null,
             var allowGeneratedReplies: Boolean = false,
             var remoteInput: RemoteInput? = null,
             var extras: Array<Bundle> = emptyArray())


