package goldzweigapps.com.core.support

import android.support.v7.widget.RecyclerView
import goldzweigapps.com.core.threads.isUiThread

abstract class GenericRecyclerViewAdapterExtensions<E : Any, VH : RecyclerView.ViewHolder>(var elements: MutableList<E>) :
        RecyclerView.Adapter<VH>() {

    override fun getItemCount() = elements.size

    open fun replaceList(newGenericList: List<E>) {
        elements.clear()
        elements.addAll(newGenericList)
        if (isUiThread()) notifyDataSetChanged()
    }

    open fun setItems(newGenericList: List<E>) = replaceList(newGenericList)

    open fun setItem(position: Int, newGenericItem: E) {
        if (position in 0..elements.size) {
            elements[position] = newGenericItem
            if (isUiThread()) notifyItemChanged(position)
        }
    }

    open fun add(itemToAdd: E, position: Int = elements.size) {
        elements.add(position, itemToAdd)
        if (isUiThread()) notifyItemInserted(position)
    }

    open fun addRange(itemsToAdd: List<E>, positionToInsert: Int = elements.size) {
        elements.addAll(positionToInsert, itemsToAdd)
        if (isUiThread()) notifyItemRangeInserted(positionToInsert, itemsToAdd.size)
    }

    open fun removeRange(itemsToRemove: List<E>) {
        val startPosition = elements.indexOf(itemsToRemove[0])
        elements.removeAll(itemsToRemove)
        if (isUiThread()) notifyItemRangeRemoved(startPosition, itemsToRemove.size)
    }

    open fun removeRange(rangeToRemove: IntRange) {
        removeRange(elements.subList(rangeToRemove.start, rangeToRemove.last))
    }

    open fun remove(itemToRemove: E) {
        val removePosition = elements.indexOf(itemToRemove)
        if (removePosition != -1) {
            elements.removeAt(removePosition)
            if (isUiThread()) notifyItemRemoved(removePosition)
        }
    }

    open fun remove(removePosition: Int) {
        if (removePosition != -1) {
            elements.removeAt(removePosition)
            if (isUiThread()) notifyItemRemoved(removePosition)
        }
    }

    open fun clear() {
        elements.clear()
        if (isUiThread()) notifyDataSetChanged()
    }

    open fun isEmpty() = elements.isEmpty()

    open fun isNotEmpty() = !isEmpty()

    open operator fun minus(rangeToRemove: IntRange) = removeRange(rangeToRemove)

    open operator fun minus(itemsToRemove: List<E>) = removeRange(itemsToRemove)
    open operator fun minus(itemToRemove: E) = remove(itemToRemove)
    open operator fun minus(removePosition: Int) = remove(removePosition)

    open operator fun plus(itemsToAdd: List<E>) = addRange(itemsToAdd)

    open operator fun plus(itemToAdd: E) = add(itemToAdd)
    open operator fun plus(itemToAddInPosition: Pair<E, Int>) =
            with(itemToAddInPosition) { add(first, second) }

    open operator fun get(position: Int) = elements[position]

    open operator fun get(item: E) = elements.indexOf(item)
}