package goldzweigapps.com.core.views

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import goldzweigapps.com.core.collections.asIterableIndexed
import goldzweigapps.com.core.threads.runInBackground

/**
 * Created by gilgoldzweig on 04/09/2017.
 */

//region view visibility
fun View.toggleVisibility() {
    visibility = if (isVisible()) View.GONE else View.VISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
//endregion view visibility

fun View.onClick(onClick: () -> Unit) = setOnClickListener { onClick.invoke() }

fun View.onLongClick(onLongClick: () -> Unit) = setOnLongClickListener {
    onLongClick.invoke()
    false
}

fun EditText.onTextChange(onTextChange: (text: CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                onTextChange(s ?: "")
    })
}



operator fun TextView.plusAssign(valueToAdd: String) {
    text = "$text$valueToAdd"
}
operator fun TextView.minusAssign(valueToRemove: String) {
    text = text.toString().removePrefix(valueToRemove)
}
operator fun TextView.contains(value: String) = value in text.toString()



fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context)
                .inflate(layoutRes, this, attachToRoot)

fun Context.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false, root: ViewGroup? = null): View =
        LayoutInflater.from(this).inflate(layoutRes, root, attachToRoot)

//region ViewGroup operators
/**
 * [position] = getChildAt(position)
 */
operator fun ViewGroup.get(position: Int) = getChildAt(position) ?: null

/**
 * [view] = indexOfChild(view)
 */
operator fun ViewGroup.get(view: View) = indexOfChild(view)

/**
 * -=
 */
operator fun ViewGroup.minusAssign(child: View) = removeView(child)

/**
 * +=
 */
operator fun ViewGroup.plusAssign(child: View) = addView(child)

/**
 * if (view in views)
 */
operator fun ViewGroup.contains(child: View) = get(child) != -1

/**
 * for (view in views.iterator)
 * @param T Any layout that extends ViewGroup for example LinearLayout
 */
operator fun <T: ViewGroup> T.iterator(): Iterable<View> {
    return asIterableIndexed({ it < childCount }, {
        getChildAt(it)
    }, {
        it.inc()
    })
}
//endregion ViewGroup operators

//region ViewGroup iterations
fun ViewGroup.first() = this[0]

fun ViewGroup.last() = this[childCount]

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    runInBackground({}, {}, {})
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachIndexed(action: (Int, View) -> Unit) {
    for (i in 0 until childCount) {
        action(i, getChildAt(i))
    }
}

inline fun ViewGroup.forEachRevered(action: (View) -> Unit) {
    for (i in (0 until childCount).reversed()) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachReveredIndexed(action: (Int, View) -> Unit) {
    for (i in (0 until childCount).reversed()) {
        action(i, getChildAt(i))
    }
}
//endregion ViewGroup iterations