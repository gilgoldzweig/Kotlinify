package goldzweigapps.com.core.preferences


import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesProperty<T : Any> internal constructor(private val defaultValue: T,
                                                        private val key: String = "",
                                                        private val background: Boolean = false):
        ReadWriteProperty<Any, T> {

    private val pref = GlobalSharedPreferences

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        val prefsKey = if (key.isEmpty()) property.name else key
        pref.put(prefsKey, value).commitOrApply()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>) =
            pref.get(if (key.isEmpty()) property.name else key, defaultValue)

    private fun GlobalSharedPreferences.commitOrApply() {
        if (background) {
            apply()
        } else {
            commit()
        }
    }
}
fun <T : Any> preferences(defaultValue: T,
                          key: String = "",
                          background: Boolean = false) =
        PreferencesProperty(defaultValue, key, background)