package goldzweigapps.com.core.preferences

import goldzweigapps.com.core.threads.RunnableThread
import goldzweigapps.com.core.threads.RunnableThreads
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesProperty<T : Any> internal constructor(private val defaultValue: T,
                                                        private val key: String = "",
                                                        private val runnableThread: RunnableThread):
        ReadWriteProperty<Any, T> {

    private val pref = GlobalSharedPreferences

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        val prefsKey = if (key.isEmpty()) property.name else key
        when (runnableThread) {

            RunnableThreads.BACKGROUND ->
                pref.put(prefsKey, value).apply()

            RunnableThreads.CURRENT ->
                pref.put(prefsKey, value)

            else ->
                runnableThread.run({ pref.put(prefsKey, value).commit() })
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>) =
            pref.get(if (key.isEmpty()) property.name else key, defaultValue)

}
fun <T : Any> preferences(defaultValue: T,
                          key: String = "",
                          thread: RunnableThread = RunnableThreads.CURRENT) =
        PreferencesProperty(defaultValue, key, thread)