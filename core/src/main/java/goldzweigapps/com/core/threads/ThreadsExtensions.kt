package goldzweigapps.com.core.threads

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper


/**
 * Created by gilgoldzweig on 04/09/2017.
 */

/**
 * @return current thread is UI thread or not
 */
fun isUiThread() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Looper.getMainLooper().isCurrentThread
        else
            Thread.currentThread() == Looper.getMainLooper().thread

