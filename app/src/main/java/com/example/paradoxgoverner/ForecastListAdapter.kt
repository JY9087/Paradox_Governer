package com.example.paradoxgoverner

import android.content.res.Resources
import android.view.*
import androidx.core.content.ContextCompat.getColor
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

        var colorful = R.color.colorCardView收入
        //根据type改变背景颜色
        when(items.get(position).type){
            "收入"->colorful = R.color.colorCardView收入
            "支出"->colorful = R.color.colorCardView支出
            "借贷"->colorful = R.color.colorCardView借贷
            "转账"->colorful = R.color.colorCardView转账
        }
        holder.view.card_view.setCardBackgroundColor( getColor(MainActivity.instance,colorful)  )
    }

    override fun getItemCount():  Int=items.size

    class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
}