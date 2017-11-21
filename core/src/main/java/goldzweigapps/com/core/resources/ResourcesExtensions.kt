package goldzweigapps.com.core.resources

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

/**
 * Created by gilgoldzweig on 04/09/2017.
 */

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.PxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Context.getDrawable(@DrawableRes drawableRes: Int) =
        ContextCompat.getDrawable(this, drawableRes)

fun Context.color(@ColorRes drawableRes: Int) =
        ContextCompat.getColor(this, drawableRes)

