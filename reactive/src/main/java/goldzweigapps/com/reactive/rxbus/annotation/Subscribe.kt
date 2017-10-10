package goldzweigapps.com.reactive.rxbus.annotation

import goldzweigapps.com.reactive.rxbus.thread.EventThread

/**
 * Marks a method as an event subscriber, as used by [AnnotatedFinder] and [Bus].
 *
 *
 *
 * The method's first (and only) parameter and tag defines the event type.
 *
 * If this annotation is applied to methods with zero parameters or more than one parameter, the object containing
 * the method will not be able to register for event delivery from the [Bus]. Bus fails fast by throwing
 * runtime exceptions in these cases.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Subscribe(val tags: Array<Tag> = [], val thread: EventThread = EventThread.MAIN_THREAD)