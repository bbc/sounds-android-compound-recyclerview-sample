package com.robpridham.compoundrecyclerviewtest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.cell_basic.view.*

class BasicViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val textView: TextView = view.text_view
}

class BasicListAdapter : RecyclerView.Adapter<BasicViewHolder>() {

    private val itemCount = 60

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellView = layoutInflater.inflate(R.layout.cell_basic, parent, false)
        return BasicViewHolder(cellView)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        holder.textView.text = "Basic item position ${position + 1}"
    }

}