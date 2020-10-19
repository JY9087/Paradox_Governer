package com.example.paradoxgoverner

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.card_view.view.*


class ForecastListAdapter(var items: List<Record>):
    RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

    companion object {
        var instance:  ForecastListAdapter by Delegates.notNull()
        fun instance() = instance
    }

    //创建View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false)
        )
    }

    //绑定数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.card_view_discription.text=items.get(position).description
        holder.view.card_view_member.text=items.get(position).member
        holder.view.card_view_time.text=items.get(position).time.toString()
        holder.view.card_view_money_amount.text=items.get(position).amount.toString()
        holder.view.card_view_date.text = items.get(position).date.toString()
        holder.view.card_view_item.text=items.get(position).item
        holder.view.card_view_merchant.text=items.get(position).merchant
        holder.view.card_view_category.text=items.get(position).category
        holder.view.card_view_subcategory.text=items.get(position).subcategory
        holder.view.card_view_type.text=items.get(position).type
    }

    override fun getItemCount():  Int=items.size

    class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
}