package goldzweigapps.com.core.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import goldzweigapps.com.core.exceptions.InitializeException
import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 04/09/2017.
 *
 */
@Suppress("unused")
object GlobalSharedPreferences  {
    private lateinit var sharedPreferences: SharedPreferences
    private var initialized = false

    /**
     * @param application Global context in order use everywhere
     *      without the need for context every time
     * @param sharedPreferencesName custom name for SharedPreferences
     * @return instance of the GlobalSharedPreferences
     */
    fun initialize(application: Application, sharedPreferencesName: String = "DefaultSharedPreferences"):
            GlobalSharedPreferences {
        sharedPreferences = application.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
            initialized = true
        return this
    }


    //region editor
    private val edit: SharedPreferences.Editor by lazy { requiredOrThrow(sharedPreferences.edit()) }
    //endregion editor

    //region get
    fun getAll(): Map<String, *> = requiredOrThrow(sharedPreferences.all)

    fun getInt(key: String, defaultValue: Int) =
            requiredOrThrow(sharedPreferences.getInt(key, defaultValue))

    fun getLong(key: String, defaultValue: Long) =
            requiredOrThrow(sharedPreferences.getLong(key, defaultValue))

    fun getFloat(key: String, defaultValue: Float) =
            requiredOrThrow(sharedPreferences.getFloat(key, defaultValue))

    fun getBoolean(key: String, defaultValue: Boolean) =
            requiredOrThrow(sharedPreferences.getBoolean(key, defaultValue))

    fun getString(key: String, defaultValue: String) : String =
            requiredOrThrow(sharedPreferences.getString(key, defaultValue))

    fun getStringSet(key: String, defaultValue: Set<String>): MutableSet<String>? =
            requiredOrThrow(sharedPreferences.getStringSet(key, defaultValue))

    infix fun String.forStringSet(defaultValue: Set<String>): MutableSet<String>? =
            requiredOrThrow(sharedPreferences.getStringSet(this, defaultValue))

    infix fun String.forInt(defaultValue: Int) =
            requiredOrThrow(sharedPreferences.getInt(this, defaultValue))

    infix fun String.forLong(defaultValue: Long) =
            requiredOrThrow(sharedPreferences.getLong(this, defaultValue))

    infix fun String.forFloat(defaultValue: Float) =
            requiredOrThrow(sharedPreferences.getFloat(this, defaultValue))

    infix fun String.forBoolean(defaultValue: Boolean) =
            requiredOrThrow(sharedPreferences.getBoolean(this, defaultValue))

    infix fun String.forString(defaultValue: String) : String =
            requiredOrThrow(sharedPreferences.getString(this, defaultValue))
    fun <T: Any> get(classType: KClass<T>, key: String) =
        when (classType) {
             String::class ->
                getString(key, "")
            Int::class ->
                getInt(key, Int.MIN_VALUE)
            Long::class ->
                getLong(key, Long.MIN_VALUE)
            Boolean::class ->
               getBoolean(key, false)
            Float::class ->
              getFloat(key, Float.MIN_VALUE)
            Set::class -> {
                getStringSet(key, emptySet())
            }
            else -> throw ClassCastException("GlobalSharedPreferences Only support SharedPreferences types")
    } as T

    //endregion get

    //region contains
    operator fun contains(key: String) = requiredOrThrow(sharedPreferences.contains(key))
    //endregion contains

    //region put
    fun put(key: String, value: String) = { edit.putString(key, value).commit() }

    fun put(key: String, value: Int) = also { edit.putInt(key, value).commit() }

    fun put(key: String, value: Long) = also { edit.putLong(key, value).commit() }

    fun put(key: String, value: Boolean) = also { edit.putBoolean(key, value).commit() }

    fun put(key: String, value: Float) = also { edit.putFloat(key, value).commit() }

    fun put(key: String, value: Set<String>) = also { edit.putStringSet(key, value).commit() }

    fun put(key: String,  value: Any) {
        also {
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
    }

    operator fun String.plusAssign(value: Any) = put(this, value)

    operator fun plusAssign(keyValuePair: Pair<String, Any>) =
           put(keyValuePair.first, keyValuePair.second)

    operator fun plus(keyValuePair: Pair<String, Any>) =
            put(keyValuePair.first, keyValuePair.second)

    //endregion put

    //region remove
    fun remove(key: String) = also { edit.remove(key).commit() }

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
    private fun <T> requiredOrThrow(returnIfInitialized: T) = if (initialized) {
        returnIfInitialized
    } else {
        throw InitializeException("GlobalSharedPreferences", "initialize")
    }

}

inline fun pref(sharedPreferences: GlobalSharedPreferences.() -> Unit) = with(GlobalSharedPreferences) {
    also {
        sharedPreferences()
        apply()
    }
}

