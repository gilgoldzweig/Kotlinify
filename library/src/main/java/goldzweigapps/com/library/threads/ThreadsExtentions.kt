package goldzweigapps.com.library.threads

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper

/**
 * Created by gilgoldzweig on 04/09/2017.
 */
private val handler = Handler()
private var backgroundTask: BackgroundTask? = null

/**
 * @return if current thread is UI thread or not
 */
fun isUiThread() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Looper.getMainLooper().isCurrentThread
        else
            Thread.currentThread() == Looper.getMainLooper().thread

/**
 * run a function after a provided millis
 * @param millis millisecond to wait before running the function
 * @param thread run the function on selected thread
 * @param func function to run after provided time
 *      runAfter(2000) {
 *        //running this part after 2000 millis in current thread
 *   }
 * @return true for success and false for failure
 */
fun runAfter(millis: Long,
             thread: RunnableThread = RunnableThread.CURRENT,
             func: () -> Unit): Boolean =
        when(thread) {
                 RunnableThread.CURRENT -> handler.postDelayed(func, millis)
                 RunnableThread.BACKGROUND -> {
                     runInBackground { handler.postDelayed(func, millis) }
                     true
             }
                 RunnableThread.UI -> runOnUI { handler.postDelayed(func, millis) }
             }
/**
 * run a function on ui thread
 * @param func
 *      runOnUI {
 *        //running this part on ui thread
 *   }
 * @return true for success and false for failure
 */
fun runOnUI(func: () -> Unit): Boolean {
    return if (!isUiThread()) {
        handler.post(func::invoke)
    } else {
        func.invoke()
        true
    }
}

/**
 * run a vararg functions on background thread
 * @param functions
 *      runInBackground ({
 *        //running this part in background
 *   },{
 *        //running also this part in background
 *   },{
 *         //running also this part in background
 *   })
 * @return Unit
 */
fun runInBackground(vararg functions: () -> Unit) {
    if (backgroundTask == null) {
        backgroundTask = BackgroundTask()
    }
    backgroundTask!!.execute(*functions)
}

/**
 * running a single function in background same as runInBackground(vararg functions: () -> Unit)
 * but with an easier lambda access
 * on background thread
 * @param func
 *      runInBackground {
 *        //running this part in background
 *   }
 * @return Unit
 */
fun runInBackground(func: () -> Unit) {
    if (backgroundTask == null) {
        backgroundTask = BackgroundTask()
    }
    backgroundTask!!.execute(func)
}

class BackgroundTask: AsyncTask<() -> Unit, Any, Any>() {
    override fun doInBackground(vararg params: (() -> Unit)?): Any {
        params.forEach { it?.invoke() }
        return false
    }
}

enum class RunnableThread {
     UI, BACKGROUND, CURRENT
}