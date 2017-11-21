package goldzweigapps.com.core.streams

import java.io.InputStream
import java.util.*

/**
 * Created by gilgoldzweig on 21/11/2017.
 */
fun InputStream.toString(): String {
    val s = Scanner(this)
            .useDelimiter("\\A")
    return if (s.hasNext()) s.next() else ""
}