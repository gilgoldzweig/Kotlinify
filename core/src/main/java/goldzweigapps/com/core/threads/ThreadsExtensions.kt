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
             thread: RunnableThread = CURRENT,
             func: () -> Unit): Boolean =
        handler.postDelayed({ thread.run(func) }, millis)

fun run(thread: RunnableThread = CURRENT, func: () -> Unit) =
        thread.run(func)


/**
 * run a function on ui thread
 * @param func
 *      runOnUI {
 *        //running this part on ui thread
 *   }
 * @return true for success and false for failure
 */
fun runOnUI(func: () -> Unit) = UI.run(func)

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
fun runInBackground(vararg functions: () -> Unit) =
        BACKGROUND.run(*functions)

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
   BACKGROUND.run(func)
}


class BackgroundTask : AsyncTask<() -> Unit, Any, Any>() {
    override fun doInBackground(vararg params: (() -> Unit)?): Any {
        params.forEach { it?.invoke() }
        return false
    }
}

interface RunnableThread {
    fun run(vararg func: () -> Unit)
}
object UI: RunnableThread {
    override fun run(vararg func: () -> Unit) {
        for (function in func) {
            if (!isUiThread()) {
                handler.post(function::invoke)
            } else {
                function.invoke()
            }
        }
    }

}
object BACKGROUND: RunnableThread {
    override fun run(vararg func: () -> Unit) {
        BackgroundTask().execute(*func)
    }
}
object CURRENT: RunnableThread {
    override fun run(vararg func: () -> Unit) {
        for (function in func) function.invoke()
    }
}
