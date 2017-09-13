package goldzweigapps.com.library.resources

import android.content.Context
import android.content.res.Resources
import android.support.graphics.drawable.VectorDrawableCompat

/**
 * Created by gilgoldzweig on 04/09/2017.
 */

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

//fun Context.getVectorDrawable() = VectorDrawableCompat.create(re)