package goldzweigapps.com.library.database

import android.database.sqlite.SQLiteDatabase

/**
 * Created by gilgoldzweig on 08/09/2017.
 */
inline fun SQLiteDatabase.inTransaction(func: SQLiteDatabase.() -> Unit) {
    beginTransaction()
    try {
        func()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }

}