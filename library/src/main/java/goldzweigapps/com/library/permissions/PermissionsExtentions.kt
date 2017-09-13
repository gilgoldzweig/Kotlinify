package goldzweigapps.com.library.permissions

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat

/**
 * Created by gilgoldzweig on 04/09/2017.
 */
@SuppressLint("NewApi")
fun isVersionAbove(version: Int) = Build.VERSION.SDK_INT >= version
@TargetApi(Build.VERSION_CODES.M)
fun isApi23OrAbove() = isVersionAbove(Build.VERSION_CODES.M)
fun isLollipopOrAbove() = isVersionAbove(Build.VERSION_CODES.LOLLIPOP)

infix fun Context.isGranted(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
