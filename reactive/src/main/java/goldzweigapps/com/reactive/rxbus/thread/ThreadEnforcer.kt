package goldzweigapps.com.reactive.rxbus.thread

import android.os.Looper

import goldzweigapps.com.reactive.rxbus.Bus

/**
 * Enforces a thread confinement policy for methods on a particular event bus.
 */
interface ThreadEnforcer {

    /**
     * Enforce a valid thread for the given `bus`. Implementations may throw any runtime exception.
     *
     * @param bus Event bus instance on which an action is being performed.
     */
    fun enforce(bus: Bus)

    companion object {


        /**
         * A [ThreadEnforcer] that does no verification.
         */
        val ANY: ThreadEnforcer = object : ThreadEnforcer {
            override fun enforce(bus: Bus) = Unit
            // Allow any thread
        }

        /**
         * A [ThreadEnforcer] that confines [Bus] methods to the main thread.
         */
        val MAIN: ThreadEnforcer = object : ThreadEnforcer {
            override fun enforce(bus: Bus) {
                if (!isUiThread()) {
                    throw IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Looper.myLooper())
                }
            }
        }
    }

}
