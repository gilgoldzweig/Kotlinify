package goldzweigapps.com.reactive.rxbus.annotation

import goldzweigapps.com.reactive.rxbus.Bus
import goldzweigapps.com.reactive.rxbus.finder.AnnotatedFinder
import goldzweigapps.com.reactive.rxbus.thread.EventThread

/**
 * Marks a method as an instance producer, as used by [AnnotatedFinder] and [Bus].
 *
 *
 * Bus infers the instance type from the annotated method's return type. Producer methods may return null when there is
 * no appropriate value to share. The calling [Bus] ignores such returns and posts nothing.
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
annotation class Produce(val tags: Array<Tag> = [], val thread: EventThread = EventThread.MAIN_THREAD)
