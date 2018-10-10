package com.bbc.sounds.compoundrecyclerviewtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.robpridham.compoundrecyclerviewtest.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        @Suppress("UNCHECKED_CAST")
        val adapter1 = BasicListAdapterImpl(R.layout.cell_basic_1, "First list item position") as? BasicListAdapter<RecyclerView.ViewHolder>

        @Suppress("UNCHECKED_CAST")
        val adapter2 = BasicListAdapterImpl(R.layout.cell_basic_2, "Second list item position") as? BasicListAdapter<RecyclerView.ViewHolder>

        val compoundListAdapter = CompoundListAdapter()
        adapter1?.let { compoundListAdapter.addChildAdapter(it) }
        adapter2?.let { compoundListAdapter.addChildAdapter(it) }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        val layoutMgr = LinearLayoutManager(this)
        layoutMgr.isAutoMeasureEnabled = false
        recyclerView.layoutManager = layoutMgr

        recyclerView.adapter = compoundListAdapter
    }
}
