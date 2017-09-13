package goldzweigapps.com.library.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * Created by gilgoldzweig on 04/09/2017.
 */
@Suppress("unused")
object GlobalSharedPreferences  {
    private lateinit var sharedPreferences: SharedPreferences
    private var initialized = false


    fun initialize(context: Context, sharedPreferencesName: String = "DefaultSharedPreferences"):
            GlobalSharedPreferences {
            sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
            initialized = true
        return this
    }


    //region editor
    @SuppressLint("CommitPrefEdits")
    private fun edit() = sharedPreferences.edit()
    //endregion editor

    //region get
    fun getAll() : Map<String, *> = sharedPreferences.all

    fun getInt(key: String, defaultValue: Int) =
            sharedPreferences.getInt(key, defaultValue)

    fun getLong(key: String, defaultValue: Long) =
            sharedPreferences.getLong(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float) =
            sharedPreferences.getFloat(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean) =
            sharedPreferences.getBoolean(key, defaultValue)

    fun getString(key: String, defaultValue: String) : String =
            sharedPreferences.getString(key, defaultValue)

    fun getStringSet(key: String, defaultValue: Set<String>): MutableSet<String>? =
            sharedPreferences.getStringSet(key, defaultValue)

    infix fun String.forStringSet(defaultValue: Set<String>): MutableSet<String>? =
            sharedPreferences.getStringSet(this, defaultValue)

    infix fun String.forInt(defaultValue: Int) =
            sharedPreferences.getInt(this, defaultValue)

    infix fun String.forLong(defaultValue: Long) =
            sharedPreferences.getLong(this, defaultValue)

    infix fun String.forFloat(defaultValue: Float) =
            sharedPreferences.getFloat(this, defaultValue)

    infix fun String.forBoolean(defaultValue: Boolean) =
            sharedPreferences.getBoolean(this, defaultValue)

    infix fun String.forString(defaultValue: String) : String =
            sharedPreferences.getString(this, defaultValue)

    //endregion get

    //region contains
    operator fun contains(key: String) = sharedPreferences.contains(key)
    //endregion contains

    //region put
    fun put(key: String, value: String): GlobalSharedPreferences {
        edit().putString(key, value)
        return this
    }

    fun put(key: String, value: Int): GlobalSharedPreferences {
        edit().putInt(key, value)
        return this
    }

    fun put(key: String, value: Long): GlobalSharedPreferences {
        edit().putLong(key, value)
        return this
    }

    fun put(key: String, value: Boolean): GlobalSharedPreferences {
        edit().putBoolean(key, value)
        return this
    }

    fun put(key: String, value: Float): GlobalSharedPreferences {
        edit().putFloat(key, value)
        return this
    }

    fun put(key: String, value: Set<String>): GlobalSharedPreferences {
        edit().putStringSet(key, value)
        return this
    }

    infix fun String.put(value: Any) {
        when(value) {
            is String ->
                put(this, value)
            is Int ->
                put(this, value)
            is Long ->
                put(this, value)
            is Boolean ->
                put(this, value)
            is Float ->
                put(this, value)
            is Set<*> -> {
                put(this, value.map { it.toString() }.toSet())
            }
        }
    }

    operator fun String.plusAssign(value: Any) = this.put(value)

    operator fun plusAssign(keyValuePair: Pair<String, Any>) =
            keyValuePair.first.put(keyValuePair.second)

    operator fun plus(keyValuePair: Pair<String, Any>) =
            keyValuePair.first.put(keyValuePair.second)

    //endregion put

    //region remove
    fun remove(key: String): GlobalSharedPreferences {
        edit().remove(key)
        return this
    }

    operator fun minus(key: String) = remove(key)
    //endregion remove

    //region commit/apply
    fun commit() = edit().commit()

    fun apply() = edit().apply()
    //endregion commit/apply

}
inline fun pref(sharedPreferences: GlobalSharedPreferences.() -> Unit) = with(GlobalSharedPreferences) {
    sharedPreferences()
    apply()
    this
}
