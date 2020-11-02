package com.example.paradoxgoverner

import android.view.*
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.card_view.view.cardview_condition_string
import kotlinx.android.synthetic.main.card_view.view.cardview_condition_type
import kotlinx.android.synthetic.main.condition_cardview.view.*


class AccountAdapter(var account: List<String>, var amount: List<Double>):
    RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    companion object {
        var instance:  AccountAdapter by Delegates.notNull()
        fun instance() = instance
    }

    //创建View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.condition_cardview, parent, false)
        )
    }

    //绑定数据
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.cardview_condition_type.text=account.get(position)
        val amountStr = String.format("%.2f",(amount.get(position)))
        holder.view.cardview_condition_string.text=" 余额："+amountStr
        //与主题颜色绑定
        var colorful = PersonalActivity.themeColor
        //holder.view.condition_cardview.setCardBackgroundColor( getColor(statisticsActivity.instance,colorful)  )
    }

    override fun getItemCount():  Int=account.size

    class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
}