package free.vpn.unblock.proxy.supervpn.vpnclient.utils

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import goldzweigapps.com.core.support.GenericRecyclerViewAdapterExtensions

abstract class GenericRecyclerAdapter<E: Any>(context: Context,
                                              genericList: MutableList<E> = ArrayList(),
                                              @LayoutRes private val layoutRes: Int) :
        GenericRecyclerViewAdapterExtensions<E, GenericRecyclerAdapter<E>.GenericViewHolder>(genericList) {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    //region setup and binding
    inner class GenericViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun onBind(currentElementPosition: Int, currentElement: E) {
            v.onBind(currentElementPosition, currentElement, this)
        }
    }

    override fun getItemCount() = elements.size

    open fun count() = itemCount

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
            GenericViewHolder(layoutInflater.inflate(layoutRes, p0, false))

    override fun onBindViewHolder(holder: GenericViewHolder, p1: Int) =
            if (elements.isNotEmpty())
                holder.onBind(p1, elements[p1]) else Unit

    abstract fun View.onBind(currentElementPosition: Int, currentElement: E, holder: GenericViewHolder)
}