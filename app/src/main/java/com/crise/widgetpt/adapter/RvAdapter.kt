package com.crise.widgetpt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crise.widgetpt.R

/**
 * 注释：
 * ===========================
 * Author by Jack
 * on 2018/7/19 15:18
 */
class RvAdapter(var context: Context, var dataList: List<String>): Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RyAdatperViewHolder(LayoutInflater.from(context).inflate(R.layout.ry_item, parent, false))
    }

    override fun getItemCount(): Int {
      return 50
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder: RyAdatperViewHolder = holder as RyAdatperViewHolder
        viewHolder.tv?.text = "lalalallala"
    }

    class RyAdatperViewHolder: RecyclerView.ViewHolder{
        var tv: TextView?= null
        constructor(itemView: View?) : super(itemView){
            tv = itemView?.findViewById(R.id.tv) as TextView?
        }
    }



}