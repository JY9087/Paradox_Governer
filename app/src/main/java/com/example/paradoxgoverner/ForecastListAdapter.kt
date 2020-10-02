package com.example.paradoxgoverner

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.activity_main.*


class ForecastListAdapter(var items: List<Record>):
    RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

    companion object {
        var instance:  ForecastListAdapter by Delegates.notNull()
        fun instance() = instance
    }

    //创建View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TextView(parent?.context))
    }

    //绑定数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.textView?.text=items.get(position).Description
    }

    override fun getItemCount():  Int=items.size

    class ViewHolder(var textView:TextView):RecyclerView.ViewHolder(textView)

}