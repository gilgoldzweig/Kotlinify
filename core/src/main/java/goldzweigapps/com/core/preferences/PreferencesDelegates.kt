package goldzweigapps.com.core.preferences

import goldzweigapps.com.core.threads.RunnableThread
import goldzweigapps.com.core.threads.RunnableThreads
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by gilgoldzweig on 03/10/2017.
 */
class PreferencesProperty<T : Any> internal constructor(private val defaultValue: T,
                                                       private val runnableThread: RunnableThread):
        ReadWriteProperty<Any, T> {

    private val pref = GlobalSharedPreferences
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        when (runnableThread) {
            RunnableThreads.BACKGROUND ->
                pref.put(property.name, value).apply()

            RunnableThreads.CURRENT ->
                pref.put(property.name, value).commit()
            else ->
                runnableThread.run({ pref.put(property.name, value).commit() })
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>) =
            pref.get(property.name, defaultValue)

}
fun <T : Any> preferences(defaultValue: T,
                                       thread: RunnableThread = RunnableThreads.CURRENT) =
        PreferencesProperty(defaultValue, thread)
