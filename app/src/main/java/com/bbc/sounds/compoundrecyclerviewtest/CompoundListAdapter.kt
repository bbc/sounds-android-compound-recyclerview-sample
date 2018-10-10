package com.bbc.sounds.compoundrecyclerviewtest

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class BasicListAdapter<VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {
    companion object {
        const val DEFAULT_VIEW_TYPE = 0
    }

    open fun getAllItemViewTypes(position: Int): Array<Int> {
        return arrayOf(DEFAULT_VIEW_TYPE)
    }
}

class CompoundListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var viewTypeRegistry = ViewTypeRegistry()

    private var lastAddedListId = 0

    private val childAdapters = mutableListOf<ChildAdapter>()

    fun addChildAdapter(adapter: BasicListAdapter<RecyclerView.ViewHolder>) {
        lastAddedListId++
        val newEntry = ChildAdapter(adapter, lastAddedListId)
        childAdapters.add(newEntry)
        val childViewTypes = adapter.getAllItemViewTypes(lastAddedListId)
        childViewTypes.forEach {
            viewTypeRegistry.registerType(lastAddedListId, it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val child = getAdapterForPosition(position)
        val positionInChild = position - child.offset
        val viewTypeInChild = child.adapter.getItemViewType(positionInChild)
        return viewTypeRegistry.getMappedViewType(child.adapter.topLevelId, viewTypeInChild)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val child = getAdapterForViewType(viewType)
        val childViewType = viewTypeRegistry.getChildViewType(viewType)
        return child.onCreateViewHolder(parent, childViewType)
    }

    override fun getItemCount(): Int {
        return childAdapters.sumBy {
            it.itemCount
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val adapterAndOffset = getAdapterForPosition(position)
        adapterAndOffset.adapter.onBindViewHolder(holder, position - adapterAndOffset.offset)
    }

    private fun getAdapterForPosition(searchPosition: Int): AdapterAndOffset {
        var startPosition = 0
        childAdapters.forEach {
            val endPosition = startPosition + it.itemCount
            if (searchPosition < endPosition) {
                return AdapterAndOffset(it, startPosition)
            }
            startPosition = endPosition
        }
        throw IllegalStateException("Nothing found at index $searchPosition")
    }

    private fun getAdapterForViewType(topLevelViewType: Int): ChildAdapter {
        val adapterId = viewTypeRegistry.getParentAdapterIdForMappedType(topLevelViewType)
        return childAdapters.first { it.topLevelId == adapterId }
    }
}

private data class AdapterAndOffset(val adapter: ChildAdapter, val offset: Int)

private class ChildAdapter(private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, val topLevelId: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = adapter.onCreateViewHolder(parent, viewType)

    override fun getItemCount(): Int = adapter.itemCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = adapter.onBindViewHolder(holder, position)
}

class ViewTypeRegistry() {

    private data class MappedViewType(val parentAdapterId: Int, val viewType: Int)

    private var lastAddedId = 1000

    private val mapOfTypes = mutableMapOf<Int, MappedViewType>()

    fun registerType(parentAdapterId: Int, viewType: Int) {
        lastAddedId++
        mapOfTypes[lastAddedId] = MappedViewType(parentAdapterId, viewType)
    }

    fun getMappedViewType(parentAdapterId: Int, viewType: Int): Int {
        return mapOfTypes.entries.first {
            it.value.parentAdapterId == parentAdapterId &&
                    it.value.viewType == viewType
        }.key
    }

    fun getChildViewType(mappedViewType: Int): Int {
        return mapOfTypes[mappedViewType]?.viewType ?: throw IllegalStateException("Not found")
    }

    fun getParentAdapterIdForMappedType(mappedViewType: Int): Int {
        return mapOfTypes[mappedViewType]?.parentAdapterId ?: throw IllegalStateException("Not found")
    }
}