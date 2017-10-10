package goldzweigapps.com.reactive.rxbus.entity


import goldzweigapps.com.reactive.rxbus.thread.EventThread
import io.reactivex.Observable
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Wraps a 'producer' method on a specific object.
 *
 *
 *
 *  This class only verifies the suitability of the method and event type if something fails.  Callers are expected
 * to verify their uses of this class.
 */
class ProducerEvent(
        /**
         * Object sporting the producer method.
         */
        val target: Any,
        /**
         * Producer method.
         */
        private val method: Method,
        /**
         * Producer thread
         */
        private val thread: EventThread) : Event() {
    /**
     * Object hash code.
     */
    /**
     * Should this producer produce events
     */
    var isValid = true
        private set

    init {
        method.isAccessible = true
        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
    }

    /**
     * If invalidated, will subsequently refuse to produce events.
     *
     *
     * Should be called when the wrapped object is unregistered from the Bus.
     */
    fun invalidate() {
        isValid = false
    }

    /**
     * Invokes the wrapped producer method and produce a [Observable].
     */
    @Throws(RuntimeException::class)
    fun produce() =
            Observable.create<Any> {
                try {
                    it.onNext(produceEvent())
                    it.onComplete()
                } catch (e: InvocationTargetException) {
                    throwRuntimeException("Producer " + this@ProducerEvent + " threw an exception.", e)
                }
            }.subscribeOn(EventThread.getScheduler(thread))

    /**
     * Invokes the wrapped producer method.
     *
     * @throws IllegalStateException     if previously invalidated.
     * @throws InvocationTargetException if the wrapped method throws any [Throwable] that is not
     * an [Error] (`Error`s are propagated as-is).
     */
    @Throws(InvocationTargetException::class)
    private fun produceEvent(): Any {
        if (!isValid) {
            throw IllegalStateException(toString() + " has been invalidated and can no longer produce events.")
        }
        try {
            return method.invoke(target)
        } catch (e: IllegalAccessException) {
            throw AssertionError(e)
        } catch (e: InvocationTargetException) {
            val cause = e.cause
            if (cause is Error) {
                throw cause
            }
            throw e
        }

    }


    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as ProducerEvent?
        return method == other!!.method && target === other.target
    }

    override fun hashCode(): Int {
        var result = target.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + thread.hashCode()
        result = 31 * result + isValid.hashCode()
        return result
    }
}
