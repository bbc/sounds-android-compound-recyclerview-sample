package com.robpridham.compoundrecyclerviewtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val adapter = BasicListAdapter(R.layout.cell_basic_1)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        val layoutMgr = LinearLayoutManager(this)
        layoutMgr.isAutoMeasureEnabled = false
        recyclerView.layoutManager = layoutMgr

        recyclerView.adapter = adapter
    }
}
