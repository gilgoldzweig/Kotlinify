package goldzweigapps.com.library.bundles

import android.os.Bundle
import android.os.Parcelable

/**
 * Created by gilgoldzweig on 12/09/2017.
 */
@Suppress("unused")
class Bundlify {
   var bundle = Bundle()
    
    //region get
    fun getAll() = bundle
    
    fun String.getStringArray(): Array<out String> =
            bundle.getStringArray(this)
    
    fun String.getStringArrayList(): ArrayList<out String> =
            bundle.getStringArrayList(this)
    
    fun String.getInt() = bundle.getInt(this)

    fun String.getLong() = bundle.getLong(this)

    fun String.getFloat() = bundle.getFloat(this)

    fun String.getBoolean()  = bundle.getBoolean(this)

    fun String.getString(): String = bundle.getString(this)
    
    fun <T: Parcelable> String.getParcelable(): T = bundle.getParcelable(this)
    fun <T: Parcelable> String.getParcelableArray() = bundle.getParcelableArray(this) as Array<T>
    fun <T: Parcelable> String.getParcelableArrayList(): ArrayList<T> =
            bundle.getParcelableArrayList<T>(this)
    //endregion get

    //region contains
    operator fun contains(key: String) = bundle.containsKey(key)
    //endregion contains

    //region put
    fun put(key: String, value: String): Bundlify {
        bundle.putString(key, value)
        return this
    }

    fun put(key: String, value: Int): Bundlify {
        bundle.putInt(key, value)
        return this
    }

    fun put(key: String, value: Long): Bundlify {
        bundle.putLong(key, value)
        return this
    }

    fun put(key: String, value: Boolean): Bundlify {
        bundle.putBoolean(key, value)
        return this
    }

    fun put(key: String, value: Float): Bundlify {
        bundle.putFloat(key, value)
        return this
    }
    fun put(key: String, value: Parcelable): Bundlify {
        bundle.putParcelable(key, value)
        return this
    }
    fun put(key: String, value: Array<Parcelable>): Bundlify {
        bundle.putParcelableArray(key, value)
        return this
    }
    fun put(key: String, value: ArrayList<Parcelable>): Bundlify {
        bundle.putParcelableArrayList(key, value)
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
            is Parcelable ->
                put(this, value)
            is Array<*> ->
                    put(this, value as Array<Parcelable>)
            is ArrayList<*> ->

                put(this, value as ArrayList<Parcelable>)
        }
    }

    operator fun String.plusAssign(value: Any) = this.put(value)

    operator fun plusAssign(keyValuePair: Pair<String, Any>) =
            keyValuePair.first.put(keyValuePair.second)

    operator fun plus(keyValuePair: Pair<String, Any>) =
            keyValuePair.first.put(keyValuePair.second)

    //endregion put

    //region remove
    fun remove(key: String): Bundlify {
        bundle.remove(key)
        return this
    }

    operator fun minus(key: String) = remove(key)
    //endregion remove
}

inline fun bundle(bundle: Bundlify.() -> Unit) = Bundlify().apply(bundle).bundle
