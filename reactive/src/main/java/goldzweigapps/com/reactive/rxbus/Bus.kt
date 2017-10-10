package goldzweigapps.com.reactive.rxbus

import goldzweigapps.com.reactive.rxbus.entity.EventType
import goldzweigapps.com.reactive.rxbus.entity.ProducerEvent
import goldzweigapps.com.reactive.rxbus.entity.SubscriberEvent
import goldzweigapps.com.reactive.rxbus.finder.Finder
import goldzweigapps.com.reactive.rxbus.thread.ThreadEnforcer
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass


/**
 * Dispatches events to listeners, and provides ways for listeners to register themselves.
 *
 *
 *
 * The Bus allows publish-subscribe-style communication between components without requiring the components to
 * explicitly register with one another (and thus be aware of each other).  It is designed exclusively to replace
 * traditional Android in-process event distribution using explicit registration or listeners. It is *not* a
 * general-purpose publish-subscribe system, nor is it intended for interprocess communication.
 *
 *
 * <h2>Receiving Events</h2>
 * To receive events, an object should:
 *
 *  1. Expose a public method, known as the *event subscriber*, which accepts a single argument of the type of event
 * desired;
 *  1. Mark it with a [Subscribe] annotation;
 *  1. Pass itself to an Bus instance's [.register] method.
 *
 *
 *
 *
 * <h2>Posting Events</h2>
 * To post an event, simply provide the event object to the [.post] or [.post] method.
 * The Bus instance will determine the type of event and route it to all registered listeners.
 *
 *
 *
 * Events are routed based on their type  and tag an event will be delivered to any subscriber for any type to which the
 * event is *assignable.*  This includes implemented interfaces, all superclasses, and all interfaces implemented
 * by superclasses.
 *
 *
 *
 * When `post` is called, all registered subscribers for an event are run in sequence, so subscribers should be
 * reasonably quick.  If an event may trigger an extended process (such as a database load), spawn a thread or queue it
 * for later.
 *
 *
 * <h2>Subscriber Methods</h2>
 * Event Subscriber methods must accept only one argument: the event.
 *
 *
 *
 * The Bus by default enforces that all interactions occur on the main thread.  You can provide an alternate
 * enforcement by passing a [ThreadEnforcer] to the constructor.
 *
 *
 * <h2>Producer Methods</h2>
 * Producer methods should accept no arguments and return their event type. When a subscriber is registered for a type
 * that a producer is also already registered for, the subscriber will be called with the return value from the
 * producer.
 *
 *
 * <h2>Dead Events</h2>
 * If an event is posted, but no registered subscribers can accept it, it is considered "dead."  To give the system a
 * second chance to handle dead events, they are wrapped in an instance of [DeadEvent] and
 * reposted.
 *
 *
 *
 * This class is safe for concurrent use.
 *
 * @author HwangJR
 */
/**
 * Test constructor which allows replacing the default `Finder`.
 *
 * @param enforcer   Thread enforcer for register, unregister, and post actions.
 * @param identifier A brief name for this bus, for debugging purposes.  Should be a valid Java identifier.
 * @param finder     Used to discover event subscribers and producers when registering/unregistering an object.
 */
class Bus internal constructor(
        /**
         * Thread enforcer for register, unregister, and posting events.
         */
        private val enforcer: ThreadEnforcer,
        /**
         * Identifier used to differentiate the event bus instance.
         */
        private val identifier: String,
        /**
         * Used to find subscriber methods in register and unregister.
         */
        private val finder: Finder) {

    /**
     * All registered event subscribers, indexed by event type.
     */
    private val subscribersByType = ConcurrentHashMap<EventType, Set<SubscriberEvent>>()

    /**
     * All registered event producers, index by event type.
     */
    private val producersByType = ConcurrentHashMap<EventType, ProducerEvent>()

    private val flattenHierarchyCache = ConcurrentHashMap<KClass<*>, Set<KClass<*>>>()

    /**
     * Creates a new Bus with the given `identifier` that enforces actions on the main thread.
     *
     * @param identifier a brief name for this bus, for debugging purposes.  Should be a valid Java identifier.
     */
    @JvmOverloads constructor(identifier: String = DEFAULT_IDENTIFIER) :
            this(ThreadEnforcer.MAIN, identifier)

    /**
     * Creates a new Bus with the given `enforcer` for actions and the given `identifier`.
     *
     * @param enforcer   Thread enforcer for register, unregister, and post actions.
     * @param identifier A brief name for this bus, for debugging purposes.  Should be a valid Java identifier.
     */
    @JvmOverloads constructor(enforcer: ThreadEnforcer, identifier: String = DEFAULT_IDENTIFIER) :
            this(enforcer, identifier, Finder.ANNOTATED)

    override fun toString() = "[Bus \"$identifier\"]"


    /**
     * Registers all subscriber methods on `any` to receive events and producer methods to provide events.
     *
     *
     * If any subscribers are registering for types which already have a producer they will be called immediately
     * with the result of calling that producer.
     *
     *
     * If any producers are registering for types which already have subscribers, each subscriber will be called with
     * the value from the result of calling the producer.
     *
     * @param `any` any whose subscriber methods should be registered.
     * @throws NullPointerException if the any is null.
     */
    fun register(any: Any) {
        enforcer.enforce(this)

        val foundProducers = finder.findAllProducers(any)
        foundProducers.forEach {
            val producer = it.value
            val previousProducer = producersByType.putIfAbsent(it.key, producer)
            //checking if the previous producer existed
            if (previousProducer != null) {
                throw IllegalArgumentException("Producer method for type " + type
                        + " found on type " + producer.getTarget().getClass()
                        + ", but already registered by type " + previousProducer.getTarget().getClass() + ".")
            }
            val subscribers = subscribersByType.get(type)
            if (subscribers != null && !subscribers.isEmpty()) {
                for (subscriber in subscribers) {
                    dispatchProducerResult(subscriber, producer)
                }
            }
        }

        val foundSubscribersMap = finder.findAllSubscribers(any)
        for (type in foundSubscribersMap.keySet()) {
            var subscribers = subscribersByType.get(type)
            if (subscribers == null) {
                //concurrent put if absent
                val SubscribersCreation = CopyOnWriteArraySet()
                subscribers = subscribersByType.putIfAbsent(type, SubscribersCreation)
                if (subscribers == null) {
                    subscribers = SubscribersCreation
                }
            }
            val foundSubscribers = foundSubscribersMap.get(type)
            if (!subscribers.addAll(foundSubscribers)) {
                throw IllegalArgumentException("Object already registered.")
            }
        }

        for (entry in foundSubscribersMap.entrySet()) {
            val type = entry.getKey()
            val producer = producersByType.get(type)
            if (producer != null && producer.isValid()) {
                val subscriberEvents = entry.getValue()
                for (subscriberEvent in subscriberEvents) {
                    if (!producer.isValid()) {
                        break
                    }
                    if (subscriberEvent.isValid()) {
                        dispatchProducerResult(subscriberEvent, producer)
                    }
                }
            }
        }
    }

    private fun dispatchProducerResult(subscriberEvent: SubscriberEvent, producer: ProducerEvent) {
        producer.produce().subscribe(object : Action1<Any>() {
            fun call(event: Any?) {
                if (event != null) {
                    dispatch(event, subscriberEvent)
                }
            }
        })
    }

    /**
     * Whether all the subscriber methods on `object` to receive events and producer methods to provide events has registered.
     *
     *
     * If any subscribers and producers has registered, it will return true, alse false.
     *
     * @param object object whose subscriber methods should be registered.
     * @throws NullPointerException if the object is null.
     */
    @Deprecated("")
    fun hasRegistered(`object`: Any?): Boolean {
        if (`object` == null) {
            throw NullPointerException("Object to register must not be null.")
        }

        var hasProducerRegistered = false
        var hasSubscriberRegistered = false
        val foundProducers = finder.findAllProducers(`object`)
        for (type in foundProducers.keySet()) {

            val producer = foundProducers.get(type)
            hasProducerRegistered = producersByType.containsValue(producer)
            if (hasProducerRegistered) {
                break
            }
        }

        if (!hasProducerRegistered) {
            val foundSubscribersMap = finder.findAllSubscribers(`object`)
            for (type in foundSubscribersMap.keySet()) {
                val subscribers = subscribersByType.get(type)
                if (subscribers != null && subscribers.size() > 0) {
                    val foundSubscribers = foundSubscribersMap.get(type)
                    // check the first subscriber, Zzzzz...
                    val foundSubscriber = if (!foundSubscribers.isEmpty()) foundSubscribers.iterator().next() else null
                    hasSubscriberRegistered = subscribers.contains(foundSubscriber)
                    if (hasSubscriberRegistered) {
                        break
                    }
                }
            }
        }
        return hasProducerRegistered || hasSubscriberRegistered
    }

    /**
     * Unregisters all producer and subscriber methods on a registered `object`.
     *
     * @param object object whose producer and subscriber methods should be unregistered.
     * @throws IllegalArgumentException if the object was not previously registered.
     * @throws NullPointerException     if the object is null.
     */
    fun unregister(`object`: Any?) {
        if (`object` == null) {
            throw NullPointerException("Object to unregister must not be null.")
        }
        enforcer.enforce(this)

        val producersInListener = finder.findAllProducers(`object`)
        for (entry in producersInListener.entrySet()) {
            val key = entry.getKey()
            val producer = getProducerForEventType(key)
            val value = entry.getValue()

            if (value == null || !value!!.equals(producer)) {
                throw IllegalArgumentException(
                        "Missing event producer for an annotated method. Is " + `object`.javaClass
                                + " registered?")
            }
            producersByType.remove(key).invalidate()
        }

        val subscribersInListener = finder.findAllSubscribers(`object`)
        for (entry in subscribersInListener.entrySet()) {
            val currentSubscribers = getSubscribersForEventType(entry.getKey())
            val eventMethodsInListener = entry.getValue()

            if (currentSubscribers == null || !currentSubscribers.containsAll(eventMethodsInListener)) {
                throw IllegalArgumentException(
                        "Missing event subscriber for an annotated method. Is " + `object`.javaClass
                                + " registered?")
            }

            for (subscriber in currentSubscribers) {
                if (eventMethodsInListener.contains(subscriber)) {
                    subscriber.invalidate()
                }
            }
            currentSubscribers.removeAll(eventMethodsInListener)
        }
    }

    /**
     * Posts an event to all registered subscribers.  This method will return successfully after the event has been posted to
     * all subscribers, and regardless of any exceptions thrown by subscribers.
     *
     *
     *
     * If no subscribers have been subscribed for `event`'s class, and `event` is not already a
     * [DeadEvent], it will be wrapped in a DeadEvent and reposted.
     *
     * @param event event to post.
     * @throws NullPointerException if the event is null.
     */
    fun post(event: Any) {
        post(Tag.DEFAULT, event)
    }

    /**
     * Posts an event to all registered subscribers.  This method will return successfully after the event has been posted to
     * all subscribers, and regardless of any exceptions thrown by subscribers.
     *
     *
     *
     * If no subscribers have been subscribed for `event`'s class, and `event` is not already a
     * [DeadEvent], it will be wrapped in a DeadEvent and reposted.
     *
     * @param tag   event tag to post.
     * @param event event to post.
     * @throws NullPointerException if the event is null.
     */
    fun post(tag: String, event: Any?) {
        if (event == null) {
            throw NullPointerException("Event to post must not be null.")
        }
        enforcer.enforce(this)

        val dispatchClasses = flattenHierarchy(event.javaClass)

        var dispatched = false
        for (clazz in dispatchClasses) {
            val wrappers = getSubscribersForEventType(EventType(tag, clazz))

            if (wrappers != null && !wrappers.isEmpty()) {
                dispatched = true
                for (wrapper in wrappers) {
                    dispatch(event, wrapper)
                }
            }
        }

        if (!dispatched && event !is DeadEvent) {
            post(DeadEvent(this, event))
        }
    }

    /**
     * Dispatches `event` to the subscriber in `wrapper`.  This method is an appropriate override point for
     * subclasses that wish to make event delivery asynchronous.
     *
     * @param event   event to dispatch.
     * @param wrapper wrapper that will call the handle.
     */
    protected fun dispatch(event: Any?, wrapper: SubscriberEvent) {
        if (wrapper.isValid) {
            wrapper.handle(event)
        }
    }

    /**
     * Retrieves the currently registered producer for `type`.  If no producer is currently registered for
     * `type`, this method will return `null`.
     *
     * @param type type of producer to retrieve.
     * @return currently registered producer, or `null`.
     */
    internal fun getProducerForEventType(type: EventType): ProducerEvent {
        return producersByType.get(type)
    }

    /**
     * Retrieves a mutable set of the currently registered subscribers for `type`.  If no subscribers are currently
     * registered for `type`, this method may either return `null` or an empty set.
     *
     * @param type type of subscribers to retrieve.
     * @return currently registered subscribers, or `null`.
     */
    internal fun getSubscribersForEventType(type: EventType): Set<SubscriberEvent>? {
        return subscribersByType.get(type)
    }

    /**
     * Flattens a class's type hierarchy into a set of Class objects.  The set will include all superclasses
     * (transitively), and all interfaces implemented by these superclasses.
     *
     * @param concreteClass class whose type hierarchy will be retrieved.
     * @return `concreteClass`'s complete type hierarchy, flattened and uniqued.
     */
    internal fun flattenHierarchy(concreteClass: Class<*>): Set<Class<*>> {
        var classes = flattenHierarchyCache.get(concreteClass)
        if (classes == null) {
            val classesCreation = getClassesFor(concreteClass)
            classes = flattenHierarchyCache.putIfAbsent(concreteClass, classesCreation)
            if (classes == null) {
                classes = classesCreation
            }
        }

        return classes
    }

    private fun getClassesFor(concreteClass: Class<*>): Set<Class<*>> {
        val parents = LinkedList()
        val classes = HashSet()

        parents.add(concreteClass)

        while (!parents.isEmpty()) {
            val clazz = parents.remove(0)
            classes.add(clazz)

            val parent = clazz.getSuperclass()
            if (parent != null) {
                parents.add(parent)
            }
        }
        return classes
    }

    companion object {
        val DEFAULT_IDENTIFIER = "default"
    }
}
/**
 * Creates a new Bus named "default" that enforces actions on the main thread.
 */
/**
 * Creates a new Bus named "default" with the given `enforcer` for actions.
 *
 * @param enforcer Thread enforcer for register, unregister, and post actions.
 */
