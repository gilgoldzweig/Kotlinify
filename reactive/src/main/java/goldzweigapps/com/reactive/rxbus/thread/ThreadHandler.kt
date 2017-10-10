package goldzweigapps.com.reactive.rxbus.thread

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object ThreadHandler {
    val executor: Executor by lazy { Executors.newCachedThreadPool() }

    val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
}

//interface ThreadHandler {
//
//
//    companion object {
//
//        val DEFAULT: ThreadHandler = object : ThreadHandler {
//            private var executor: Executor? = null
//            private var handler: Handler? = null
//
//            override fun getExecutor(): Executor {
//                if (executor == null) {
//                    executor = Executors.newCachedThreadPool()
//                }
//                return executor
//            }
//
//            override fun getHandler(): Handler {
//                if (handler == null) {
//                    handler = Handler(Looper.getMainLooper())
//                }
//                return handler
//            }
//        }
//    }
//}
