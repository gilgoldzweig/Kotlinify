package goldzweigapps.com.core.bundles

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by gilgoldzweig on 16/10/2017.
 */
class BundlifyDelegate<T: Any>(val bundlify: Bundlify): ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>) =
            bundlify.get(thisRef::class, property.name) as T

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            bundlify.put(property.name, value)
}