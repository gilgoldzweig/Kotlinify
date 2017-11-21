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
             thread: RunnableThread = RunnableThreads.CURRENT,
             func: () -> Unit) =
        handler.postDelayed({ thread.run(func) }, millis)

fun run(thread: RunnableThread = RunnableThreads.CURRENT, func: () -> Unit) =
        thread.run(func)


/**
 * run a function on ui thread
 * @param func
 *      runOnUI {
 *        //running this part on ui thread
 *   }
 * @return true for success and false for failure
 */
fun runOnUI(func: () -> Unit) = RunnableThreads.UI.run(func)

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
        RunnableThreads.BACKGROUND.run(*functions)

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
fun runInBackground(func: () -> Unit) = RunnableThreads.BACKGROUND.run(func)

class BackgroundReturnTask<R>(val returnValue: (R) -> Unit) : AsyncTask<() -> R, Any, Any>() {
    var values: List<R> = ArrayList()
            override fun doInBackground(vararg params: (() -> R)): Any {
                params.forEach { values += it.invoke() }
                return false
            }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        values.forEach(returnValue::invoke)
    }
}

fun <R: Any?> runInBackground(func: () -> R, returnValue: (R) -> Unit) {
    BackgroundReturnTask(returnValue).execute(func)
}

interface RunnableThread {
    fun run(vararg functions: () -> Unit)
}

sealed class RunnableThreads: RunnableThread {
    object UI : RunnableThreads() {
        override fun run(vararg functions: () -> Unit) {
            for (function in functions) {
                if (!isUiThread()) {
                    handler.post(function::invoke)
                } else {
                    function.invoke()
                }
            }
        }
    }
    object BACKGROUND : RunnableThreads() {
        override fun run(vararg functions: () -> Unit) {
            functions.forEach { Thread(it::invoke).start() }
        }
    }
    object CURRENT : RunnableThreads() {
        override fun run(vararg functions: () -> Unit) {
            for (function in functions) function.invoke()
        }
    }
}

