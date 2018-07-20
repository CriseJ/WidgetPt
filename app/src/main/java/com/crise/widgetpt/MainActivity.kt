package com.crise.widgetpt

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.crise.widgetpt.adapter.RvAdapter
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {

    var ryAdapter: RvAdapter?=null
    var dataLists: MutableList<String>? = mutableListOf()
    lateinit var swipRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var layoutManager: LinearLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager
        ryAdapter = RvAdapter(this@MainActivity, this!!.dataLists!!)
        rv.adapter = ryAdapter



    }










}
