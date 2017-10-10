package goldzweigapps.com.reactive.rxbus.annotation

import goldzweigapps.com.reactive.rxbus.Bus
import goldzweigapps.com.reactive.rxbus.finder.AnnotatedFinder

/**
 * Marks the tags for a subscriber, as used by [AnnotatedFinder] and [Bus].
 *
 *
 *
 * The tag's default value is `Tag.DEFAULT`.
 *
 * If this annotation is applied to subscriber with none parameter or more than one parameter, Bus will
 * delivery the events(tag and method's first (and only) parameter).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Tag(val value: String = DEFAULT)

const val DEFAULT = "rxbus_default_tag"
