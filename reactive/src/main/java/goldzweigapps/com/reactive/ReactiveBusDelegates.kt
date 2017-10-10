package goldzweigapps.com.reactive

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by gilgoldzweig on 09/10/2017.
 */
class ReactiveBusDelegates<T : Any> private constructor() : ReadWriteProperty<Any, T?> {
    var type: T? = null
    override fun getValue(thisRef: Any, property: KProperty<*>) = type

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}