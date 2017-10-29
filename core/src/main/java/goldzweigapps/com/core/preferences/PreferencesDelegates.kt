package goldzweigapps.com.core.preferences

import goldzweigapps.com.core.threads.CURRENT
import goldzweigapps.com.core.threads.RunnableThread
import goldzweigapps.com.core.threads.run
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by gilgoldzweig on 03/10/2017.
 */
class PreferencesProperty<T : Any>(val runnableThread: RunnableThread): ReadWriteProperty<Any, T> {
    val pref = GlobalSharedPreferences
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        run(runnableThread) { pref.put(property.name, value) }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>)  =
            pref.get(thisRef::class, property.name) as T
}
inline fun <reified T:Any> preferences(thread: RunnableThread = CURRENT) =
        PreferencesProperty<T>(thread)