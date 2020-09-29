package com.example.paradoxgoverner

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ForecastListAdapter(var items: List<Record>):
    RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

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