package goldzweigapps.com.core.threads

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper

/**
 * Created by gilgoldzweig on 04/09/2017.
 */
private val handler = Handler()

/**
 * @return current thread is UI thread or not
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
                handler.postDelayed({ runInBackground { func.invoke() } }, millis)
                true
            }
            RunnableThread.UI -> handler.postDelayed({ runOnUI { func.invoke() } }, millis)
        }

fun run(thread: RunnableThread = RunnableThread.CURRENT,
        func: () -> Unit) = when (thread) {
    RunnableThread.CURRENT -> func.invoke()
    RunnableThread.BACKGROUND -> {
        runInBackground { func.invoke() }

    }
    RunnableThread.UI -> {
        runOnUI { func.invoke() }
        Unit
    }
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
    BackgroundTask().execute(*functions)
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
    BackgroundTask().execute(func)
}


class BackgroundTask : AsyncTask<() -> Unit, Any, Any>() {
    override fun doInBackground(vararg params: (() -> Unit)?): Any {
        params.forEach { it?.invoke() }
        return false
    }
}

enum class RunnableThread {
     UI, BACKGROUND, CURRENT
}

