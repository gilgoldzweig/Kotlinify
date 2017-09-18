package goldzweigapps.com.core.support

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by gilgoldzweig on 12/09/2017.
 */
@SuppressLint("ValidFragment")
class FragmentDSL(private @LayoutRes val layoutRes: Int): Fragment() {

    var onViewCreated: ((view: View, context: Context, savedInstanceState: Bundle?) -> Unit)? = null
    fun onViewCreated(onViewCreated: (view: View, context: Context, savedInstanceState: Bundle?) -> Unit) {
        this.onViewCreated = onViewCreated
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        view?.let {
            onViewCreated?.invoke(it, context, savedInstanceState)
        }

    }
}
fun fragment(@LayoutRes layoutRes: Int, init: FragmentDSL.() -> Unit) =
    FragmentDSL(layoutRes).apply(init)
