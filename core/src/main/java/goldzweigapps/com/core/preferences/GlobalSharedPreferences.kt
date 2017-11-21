package goldzweigapps.com.core.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import goldzweigapps.com.core.exceptions.InitializeException

/**
 * Created by gilgoldzweig on 04/09/2017.
 *
 */
@Suppress("unused")
object GlobalSharedPreferences {
    private lateinit var sharedPreferences: SharedPreferences
    private var initialized = false

    /**
     * @param application Global context in order use everywhere
     *      without the need for context every time
     * @param sharedPreferencesName custom name for SharedPreferences
     * @return instance of the GlobalSharedPreferences
     */
  fun initialize(application: Application, sharedPreferencesName: String = ""):
            GlobalSharedPreferences {
        if (!initialized) {
            sharedPreferences = if (sharedPreferencesName.isEmpty()) {
                PreferenceManager.getDefaultSharedPreferences(application)
            } else {
                application.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
            }
            initialized = true
        }
        return this
    }


    //region editor
    private val edit: SharedPreferences.Editor by lazy { requiredOrThrow(sharedPreferences::edit) }
    //endregion editor

    //region get
    fun getAll(): Map<String, *> = requiredOrThrow(sharedPreferences.all)

    fun getInt(key: String, defaultValue: Int = -1) =
            requiredOrThrow(sharedPreferences.getInt(key, defaultValue))

    fun getLong(key: String, defaultValue: Long = -1) =
            requiredOrThrow(sharedPreferences.getLong(key, defaultValue))

    fun getFloat(key: String, defaultValue: Float = -1F) =
            requiredOrThrow(sharedPreferences.getFloat(key, defaultValue))

    fun getBoolean(key: String, defaultValue: Boolean = false) =
            requiredOrThrow(sharedPreferences.getBoolean(key, defaultValue))

    fun getString(key: String, defaultValue: String = "") : String =
            requiredOrThrow(sharedPreferences.getString(key, defaultValue))

    fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): MutableSet<String>? =
            requiredOrThrow(sharedPreferences.getStringSet(key, defaultValue))

    fun String.forStringSet(defaultValue: Set<String> = emptySet()): MutableSet<String>? =
            requiredOrThrow(sharedPreferences.getStringSet(this, defaultValue))

    fun String.forInt(defaultValue: Int = -1) =
            requiredOrThrow(sharedPreferences.getInt(this, defaultValue))

    fun String.forLong(defaultValue: Long = -1) =
            requiredOrThrow(sharedPreferences.getLong(this, defaultValue))

    fun String.forFloat(defaultValue: Float = -1F) =
            requiredOrThrow(sharedPreferences.getFloat(this, defaultValue))

    fun String.forBoolean(defaultValue: Boolean = false) =
            requiredOrThrow(sharedPreferences.getBoolean(this, defaultValue))

    fun String.forString(defaultValue: String = "") : String =
            requiredOrThrow(sharedPreferences.getString(this, defaultValue))

    fun <T: Any> get(key: String, defaultValue: T) =
        when (defaultValue) {
            is String -> getString(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            is Long -> getLong(key, defaultValue)
            is Boolean -> getBoolean(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            is Set<*> -> getStringSet(key, defaultValue as Set<String>)
            else -> throw ClassCastException("""
                GlobalSharedPreferences Only support SharedPreferences types.
                provided type: ${defaultValue::class}
                value: $defaultValue
            """.trimIndent())
        } as T

    //endregion get

    //region contains
    operator fun contains(key: String) = requiredOrThrow(sharedPreferences.contains(key))
    //endregion contains

    //region put
    fun put(key: String, value: String) = also { edit.putString(key, value) }

    fun put(key: String, value: Int) = also { edit.putInt(key, value) }

    fun put(key: String, value: Long) = also { edit.putLong(key, value) }

    fun put(key: String, value: Boolean) = also { edit.putBoolean(key, value) }

    fun put(key: String, value: Float) = also { edit.putFloat(key, value) }

    fun put(key: String, value: Set<String>) = also { edit.putStringSet(key, value) }

    fun put(key: String,  value: Any) = also {
            when (value) {
                is String ->
                    put(key, value)
                is Int ->
                    put(key, value)
                is Long ->
                    put(key, value)
                is Boolean ->
                    put(key, value)
                is Float ->
                    put(key, value)
                is Set<*> ->
                    put(key, value.map { it.toString() }.toSet())
            }
        }
    internal fun putUnit(key: String,  value: Any) {
        put(key, value)
    }

    operator fun String.plusAssign(value: Any) = putUnit(this, value)

    operator fun plusAssign(keyValuePair: Pair<String, Any>) =
            putUnit(keyValuePair.first, keyValuePair.second)

    operator fun plus(keyValuePair: Pair<String, Any>) =
            put(keyValuePair.first, keyValuePair.second)

    //endregion put

    //region remove
    fun remove(key: String) = also { edit.remove(key) }

    operator fun minus(key: String) = remove(key)
    //endregion remove

    //region commit/apply
    fun commit() = edit.commit()

    fun apply() = edit.apply()
    //endregion commit/apply

    /**
     * @param returnIfInitialized object to be returned if class is initialized
     * @throws InitializeException
     */
    @Throws(InitializeException::class)
    private fun <T> requiredOrThrow(returnIfInitialized: T): T = synchronized(initialized) {
        if (initialized)
             returnIfInitialized
        else
            throw InitializeException("GlobalSharedPreferences", "initialize")
    }
        /**
         * @param returnIfInitialized object to be returned if class is initialized
         * @throws InitializeException
         */
        @Throws(InitializeException::class)
        private fun <T> requiredOrThrow(returnIfInitialized: () -> T): T =
                GlobalSharedPreferences.requiredOrThrow { returnIfInitialized.invoke() }
    }

inline fun pref(sharedPreferences: GlobalSharedPreferences.() -> Unit) = with(GlobalSharedPreferences) {
    also {
        sharedPreferences()
        apply()
    }
}

