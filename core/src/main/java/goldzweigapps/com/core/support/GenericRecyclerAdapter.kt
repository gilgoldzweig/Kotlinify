package goldzweigapps.com.core.support

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import goldzweigapps.com.core.threads.isUiThread

/**
 * Created by gilgoldzweig on 05/09/2017.
 */


@Suppress("unused")
abstract class GenericRecyclerAdapter<E>(context: Context,
                                         var genericList: ArrayList<E> = ArrayList(),
                                         @LayoutRes private val layoutRes: Int) :
        RecyclerView.Adapter<GenericRecyclerAdapter<E>.GenericViewHolder>() {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    //region setup and binding
    inner class GenericViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun onBind(currentElementPosition: Int, currentElement: E) {
            v.onBind(currentElementPosition, currentElement, this)
        }
    }

    override fun getItemCount() = genericList.size

    open fun count() = itemCount

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int) =
            GenericViewHolder(layoutInflater.inflate(layoutRes, p0, false))


    override fun onBindViewHolder(holder: GenericViewHolder?, p1: Int) =
            if (holder != null && genericList.isNotEmpty() && genericList[p1] != null)
                holder.onBind(p1, genericList[p1]) else Unit


    abstract fun View.onBind(currentElementPosition: Int, currentElement: E, holder: GenericViewHolder)
    //endregion setup and binding

    //region open functions
    //region replacement functions
    open fun replaceList(newGenericList: List<E>) {
        genericList = ArrayList(newGenericList)
        if (isUiThread()) notifyDataSetChanged()
    }

    open fun setItems(newGenericList: List<E>) = replaceList(newGenericList)

    open fun setItem(position: Int, newGenericItem: E) {
        genericList[position] = newGenericItem
        if (isUiThread()) notifyItemChanged(position)
    }
    //endregion replacement functions

    //region insertion functions
    open fun add(itemToAdd: E, position: Int = genericList.size) {
        genericList.add(position, itemToAdd)
        if (isUiThread()) notifyItemInserted(position)
    }

    open fun addRange(itemsToAdd: List<E>, positionToInsert: Int = genericList.size) {
        genericList.addAll(positionToInsert, itemsToAdd)
        if (isUiThread()) notifyItemRangeInserted(positionToInsert, itemsToAdd.size)
    }
    //endregion insertion functions

    //region removal functions
    open fun removeRange(itemsToRemove: List<E>) {
        val startPosition = genericList.indexOf(itemsToRemove[0])
        genericList.removeAll(itemsToRemove)
        if (isUiThread()) notifyItemRangeRemoved(startPosition, itemsToRemove.size)
    }

    open fun removeRange(rangeToRemove: IntRange) {
        removeRange(genericList.subList(rangeToRemove.start, rangeToRemove.last))
    }

    open fun remove(itemToRemove: E) {
        val removePosition = genericList.indexOf(itemToRemove)
        if (removePosition != -1) {
            genericList.removeAt(removePosition)
            if (isUiThread()) notifyItemRemoved(removePosition)
        }
    }

    open fun remove(removePosition: Int) {
        if (removePosition != -1) {
            genericList.removeAt(removePosition)
            if (isUiThread()) notifyItemRemoved(removePosition)
        }
    }

    open fun clear() {
        genericList.clear()
        if (isUiThread()) notifyDataSetChanged()
    }
    //endregion removal functions

    //region empty checks
    open fun isEmpty() = genericList.isEmpty()

    open fun isNotEmpty() = !isEmpty()
    //endregion empty checks

    //region operators
    //region minus operators
    open operator fun minus(rangeToRemove: IntRange) = removeRange(rangeToRemove)
    open operator fun minus(itemsToRemove: List<E>) = removeRange(itemsToRemove)
    open operator fun minus(itemToRemove: E) = remove(itemToRemove)
    open operator fun minus(removePosition: Int) = remove(removePosition)
    //endregion minus operators

    //region plus operators
    open operator fun plus(itemsToAdd: List<E>) = addRange(itemsToAdd)
    open operator fun plus(itemToAdd: E) = add(itemToAdd)
    open operator fun plus(itemToAddInPosition: Pair<E, Int>) =
            with(itemToAddInPosition) { add(first, second) }
    //endregion plus operators

    //region get operators
    open operator fun get(position: Int) = genericList[position]
    open operator fun get(item: E) = genericList.indexOf(item)
    //endregion get operators
    //endregion operators
    //endregion open functions
}

