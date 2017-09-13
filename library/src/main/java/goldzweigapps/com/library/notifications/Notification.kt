package goldzweigapps.com.library.notifications

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat


/**
 * Created by gilgoldzweig on 04/09/2017.
 */
class Notification(context: Context, channelId: String) :
        android.app.Notification.Builder(context, channelId) {
    var title: String = ""
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
        setChannelId(id)
        return notificationChannel
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
        setActions(*actions.toTypedArray())
        return notificationAction
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
inline fun Context.notification(channelId: String, notificationId: Int,
                                notification: Notification.() -> Unit): android.app.Notification =
        Notification(this, channelId)
                .apply(notification)
                .build()





class Action(var title: CharSequence = "",
             var icon: Icon? = null,
             var intent: PendingIntent? = null,
             var allowGeneratedReplies: Boolean = false,
             var remoteInput: RemoteInput? = null,
             var extras: Array<Bundle> = emptyArray())


