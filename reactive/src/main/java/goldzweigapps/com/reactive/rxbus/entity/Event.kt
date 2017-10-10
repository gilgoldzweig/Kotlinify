package goldzweigapps.com.reactive.rxbus.entity

import java.lang.reflect.InvocationTargetException

abstract class Event {
    /**
     * Throw a [RuntimeException] with given message and cause lifted from an [ ]. If the specified [InvocationTargetException] does not have a
     * cause, neither will the [RuntimeException].
     */
    fun throwRuntimeException(msg: String, e: InvocationTargetException) {
        throwRuntimeException(msg, e.cause)
    }

    /**
     * Throw a [RuntimeException] with given message and cause lifted from an [ ]. If the specified [InvocationTargetException] does not have a
     * cause, neither will the [RuntimeException].
     */
    fun throwRuntimeException(msg: String, e: Throwable?) {
        val cause = e?.cause
        if (cause != null) {
            throw RuntimeException(msg + ": " + cause.message, cause)
        } else {
            throw RuntimeException(msg + ": " + e?.message, e)
        }
    }
}