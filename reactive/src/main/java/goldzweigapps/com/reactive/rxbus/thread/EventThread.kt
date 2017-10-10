package goldzweigapps.com.reactive.rxbus.thread

import android.os.Handler
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor


enum class EventThread {
    /**
     * [Scheduler] which will execute actions on the Android UI thread.
     */
    MAIN_THREAD,

    /**
     * Creates and returns a [Scheduler] that creates a new [Thread] for each unit of work.
     *
     *
     * Unhandled errors will be delivered to the scheduler Thread's [Thread.UncaughtExceptionHandler].
     */
    NEW_THREAD,

    /**
     * Creates and returns a [Scheduler] intended for IO-bound work.
     *
     *
     * The implementation is backed by an [Executor] thread-pool that will grow as needed.
     *
     *
     * This can be used for asynchronously performing blocking IO.
     *
     *
     * Do not perform computational work on this scheduler. Use computation() instead.
     *
     *
     * Unhandled errors will be delivered to the scheduler Thread's [Thread.UncaughtExceptionHandler].
     */
    IO,

    /**
     * Creates and returns a [Scheduler] intended for computational work.
     *
     *
     * This can be used for event-loops, processing callbacks and other computational work.
     *
     *
     * Do not perform IO-bound work on this scheduler. Use io() instead.
     *
     *
     * Unhandled errors will be delivered to the scheduler Thread's [Thread.UncaughtExceptionHandler].
     */
    COMPUTATION,

    /**
     * Creates and returns a [Scheduler] that queues work on the current thread to be executed after the
     * current work completes.
     */
    TRAMPOLINE,

    /**
     * Creates and returns a [Scheduler] that executes work immediately on the current thread.
     */
    IMMEDIATE,

    /**
     * Converts an [Executor] into a new Scheduler instance.
     */
    EXECUTOR,

    /**
     * [Scheduler] which uses the provided [Handler] to execute actions.
     */
    HANDLER;

    companion object {
        fun getScheduler(thread: EventThread): Scheduler =
                when (thread) {
                    MAIN_THREAD -> AndroidSchedulers.mainThread()
                    NEW_THREAD -> Schedulers.newThread()
                    IO -> Schedulers.io()
                    COMPUTATION -> Schedulers.computation()
                    TRAMPOLINE, IMMEDIATE -> Schedulers.trampoline()
                    EXECUTOR -> Schedulers.from(ThreadHandler.executor)
                //                HANDLER -> scheduler = ThreadHandler.from(ThreadHandler.handler)
                    else -> AndroidSchedulers.mainThread()
                }
    }
}
