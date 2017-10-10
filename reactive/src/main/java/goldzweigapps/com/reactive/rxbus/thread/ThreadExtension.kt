package goldzweigapps.com.reactive.rxbus.thread

import android.os.Build
import android.os.Looper

/**
 * Created by gilgoldzweig on 10/10/2017.
 */
fun isUiThread() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Looper.getMainLooper().isCurrentThread
        else
            Thread.currentThread() == Looper.getMainLooper().thread