package com.bbc.sounds.compoundrecyclerviewtest

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.cell_basic_1.view.*

class BasicViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val textView: TextView = view.text_view
}

class BasicListAdapterImpl(@LayoutRes val layout: Int, val prefixText: String) : BasicListAdapter<BasicViewHolder>() {
    private val itemCount = 60

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(layout, parent, false)
        return BasicViewHolder(cellView)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        val displayPosition = position + 1
        holder.textView.text = "$prefixText $displayPosition"
    }
}