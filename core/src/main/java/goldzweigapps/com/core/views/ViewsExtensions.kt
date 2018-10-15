package goldzweigapps.com.core.views

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import goldzweigapps.com.core.collections.asIterableIndexed

/**
 * Created by gilgoldzweig on 04/09/2017.
 */

//region view visibility
fun View.toggleVisibility() {
    visibility = if (visible) View.GONE else View.VISIBLE
}

inline val View.visible
    get() = visibility == View.VISIBLE

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

fun View.onClick(onClick: () -> Unit) = setOnClickListener { onClick() }

fun View.onLongClick(onLongClick: () -> Boolean) = setOnLongClickListener {
    onLongClick()
}

fun EditText.onTextChange(onTextChange: (text: CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                onTextChange(s ?: "")
    })
}

typealias ActionString = Pair<CharSequence, View.OnClickListener>
typealias ActionResId = Pair<Int, View.OnClickListener>

val Context.linearLayoutManager: LinearLayoutManager
    get() = LinearLayoutManager(this)

val Context.horizontalLinearLayoutManager: LinearLayoutManager
    get() = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

fun View.snackBar(message: CharSequence,
                  length: Int = Snackbar.LENGTH_LONG,
                  action: ActionString? = null) {
    val snackBar = Snackbar.make(this, message, length)
    action?.let {
        snackBar.setAction(it.first, it.second)
    }
    snackBar.show()
}

fun View.snackBar(@StringRes message: Int,
                  length: Int = Snackbar.LENGTH_LONG,
                  action: ActionResId? = null) {
    val snackBar = Snackbar.make(this, message, length)
    action?.let {
        snackBar.setAction(it.first, it.second)
    }
    snackBar.show()
}


inline var View.layoutHeigth: Int
    get() = layoutParams.height
    set(value) {
        layoutParams.height = value
    }

inline var View.layoutWidth: Int
    get() = layoutParams.width
    set(value) {
        layoutParams.width = value
    }

operator fun TextView.plusAssign(valueToAdd: String) {
    text = "$text$valueToAdd"
}

operator fun TextView.minusAssign(valueToRemove: String) {
    text = text.removePrefix(valueToRemove)
}

operator fun TextView.contains(value: String) = value in text

@Throws(IndexOutOfBoundsException::class)
operator fun TextView.get(index: Int): Char {
    return if (index in 0..text.length) {
        text[index]
    } else {
        throw IndexOutOfBoundsException("""
            Index: $index
            Start: 0
            End: ${text.length}
        """.trimIndent())
    }
}

operator fun TextView.get(char: Char, ignoreCase: Boolean = false) =
        text.toString().indexOf(char, 0, ignoreCase)

fun Context.inflate(@LayoutRes layoutRes: Int,
                    attachToRoot: Boolean = false,
                    root: ViewGroup? = null): View =
        LayoutInflater.from(this).inflate(layoutRes, root, attachToRoot)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int,
                      attachToRoot: Boolean = false): View =
        context.inflate(layoutRes, attachToRoot, this)


//region ViewGroup operators
/**
 * [position] = getChildAt(position)
 */
operator fun ViewGroup.get(position: Int): View? = getChildAt(position)

/**
 * [view] = indexOfChild(view)
 */
operator fun ViewGroup.get(view: View): Int = indexOfChild(view)

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
operator fun ViewGroup.contains(child: View): Boolean = this[child] != -1

/**
 * for (view in views.iterator)
 * @param T Any layout that extends ViewGroup for example LinearLayout
 */
operator fun <T : ViewGroup> T.iterator(): Iterable<View> {
    return asIterableIndexed({ it < childCount }, {
        getChildAt(it)
    })
}
//endregion ViewGroup operators

//region ViewGroup iterations
fun ViewGroup.first() = this[0]

fun ViewGroup.last() = this[childCount]

inline fun ViewGroup.forEach(action: (View) -> Unit) = iterator().forEach(action)

inline fun ViewGroup.forEachIndexed(action: (Int, View) -> Unit) = iterator().forEachIndexed(action)

inline fun ViewGroup.forEachRevered(action: (View) -> Unit) {
    for (i in childCount downTo 0) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachReveredIndexed(action: (Int, View) -> Unit) {
    for (i in childCount downTo 0) {
        action(i, getChildAt(i))
    }
}
//endregion ViewGroup iterations