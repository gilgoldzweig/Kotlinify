package goldzweigapps.com.core.collections

import goldzweigapps.com.core.other.ignoreResult
import java.util.*
import kotlin.contracts.*

/**
 * Created by gilgoldzweig on 04/09/2017.
 */

private val random by lazy { Random() }


@ExperimentalContracts
fun <E> Collection<E>?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty is Collection<E>)
    }
    return this == null || isEmpty()
}

@ExperimentalContracts
fun <E> Collection<E>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty is Collection<E>)
    }
    return !isNullOrEmpty()
}

operator fun <E> Collection<E>.get(element: E): Int = indexOf(element)

fun <E> List<E>.random(): E = this[random.nextInt(size)]

infix fun <E> MutableCollection<E>.addIfNotExist(element: E): Boolean =
        if (!contains(element)) add(element) else false

infix fun <E> MutableCollection<E>.removeIfExist(element: E): Boolean =
        if (contains(element)) remove(element) else false

infix fun <E> MutableCollection<E>.addUnique(element: E): Boolean =
        addIfNotExist(element)

infix fun <E> MutableCollection<E>.removeSafely(element: E): Boolean =
        removeIfExist(element)

operator fun <E> MutableCollection<E>.plusAssign(element: E): Unit = add(element).ignoreResult()

@ExperimentalContracts
fun <K, V> Map<K, V>?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty is Map<K, V>)
    }
    return this == null || isEmpty()
}


@ExperimentalContracts
fun <K, V> Map<K, V>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty is Map<K, V>)
    }
    return !isNullOrEmpty()
}

fun <T, E> E.asIterable(hasNext: E.() -> Boolean, next: E.() -> T): Iterable<T> =
        object : Iterable<T> {
            override fun iterator(): Iterator<T> {
                return object : Iterator<T> {
                    override fun hasNext() = hasNext(this@asIterable)
                    override fun next() = next(this@asIterable)
                }
            }
        }


fun <T, E> E.asIterable(hasNext: Boolean, next: (it: E) -> T): Iterable<T> =
        asIterable({ hasNext }, { next(this) })

fun <T, E> E.asIterableIndexed(hasNext: E.(index: Int) -> Boolean,
                               next: E.(index: Int) -> T,
                               increaseOnNext: Boolean = true): Iterable<T> {
    var index = 0

    return asIterable(this@asIterableIndexed.hasNext(index)) {
        val element = next(index)
        if (increaseOnNext) {
            index++
        }
        element
    }
}

fun <T, E> E.asIterableIndexed(hasNext: Boolean,
                               next: E.(index: Int) -> T,
                               increaseOnNext: Boolean = true): Iterable<T> =
        asIterableIndexed({ hasNext }, next, increaseOnNext)

operator fun <E> Iterable<E>.get(e: E) = firstOrNull { it == e }

operator fun <E> Iterable<E>.get(position: Int): E? {
    if (position < 0) return null
    forEachIndexed { index, element ->
        if (index == position) return element
    }
    return null
}