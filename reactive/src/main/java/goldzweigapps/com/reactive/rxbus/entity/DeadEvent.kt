package goldzweigapps.com.reactive.rxbus.entity


/**
 * Wraps an event that was posted, but which had no subscribers and thus could not be delivered.
 *
 *
 *
 * Subscribing a DeadEvent is useful for debugging or logging, as it can detect misconfigurations in a
 * system's event distribution.
 */

/**
 * Creates a new DeadEvent.
 *
 * @param source object broadcasting the DeadEvent (generally the [Bus]).
 * @param event  the event that could not be delivered.
 */
class DeadEvent(val source: Any, val event: Any)
