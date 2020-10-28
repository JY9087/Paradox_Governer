package com.example.paradoxgoverner

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.statistics.*
import java.util.*
import java.sql.Date

class StatisticActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)

        val memberStringList = mutableListOf<String>()
        val categoryStringList = mutableListOf<String>()
        val subcategoryStringList = mutableListOf<String>()
        val subcategoryStringListSpinner = mutableListOf<String>()
        for (members in AppDatabase.instance.userDAO().getAllMember()) {
            memberStringList.add(members.member)
        }
        for (category in AppDatabase.instance.userDAO().getAllCategory()) {
            categoryStringList.add(category.category)
        }
        for (category in categoryStringList) {
            for (subcategory in AppDatabase.instance.userDAO().getAllSubcategory(category))
            {
                subcategoryStringList.add(subcategory.subcategory)
            }
        }
        val itemStringList = mutableListOf<String>()
        val merchantStringList = mutableListOf<String>()
        for (item in AppDatabase.instance.userDAO().getAllItem()) {
            itemStringList.add(item.item)
        }
        for (merchant in AppDatabase.instance.userDAO().getAllMerchant()) {
            merchantStringList.add(merchant.merchant)
        }
        //查找所需要的数据包，返回值是一个List<Record>
        fun selectData(
            needAccount: String,
            needCategory: List<String> = categoryStringList.toList(), //默认参数，即全部Category
            needSubcategory: List<String> = subcategoryStringList.toList(),
            needMember: List<String> = memberStringList.toList(),
            startDate: Date,//java.util
            endDate: Date, //java.util
            income: Boolean,
            needItem: List<String> = itemStringList.toList(),
            needMerchant: List<String> = merchantStringList.toList(),
            needType: List<String> = DEFAULT_TYPE_LIST
        ): List<Record> {
            val start:java.sql.Date = java.sql.Date(startDate.time)
            val end:java.sql.Date = java.sql.Date(endDate.time)
            return AppDatabase.instance.userDAO().getSubCategoryBill(
                needAccount,
                start.time,
                end.time + 86400000,
                needMember,
                needCategory,
                needSubcategory,
                income,
                needItem,
                needMerchant,
                needType
            )
        }

        //给出一个List<Record>，求其金额之和
        fun getSumAmount(SelectList: List<Record>):Float{
            var sumAmount = 0F
            for (item in SelectList)
            {
                sumAmount += item.amount
            }
            return sumAmount
        }

        //流水查询，Desc为True为降序排列，否则为升序排列
        fun getWaterBill(needAccount: String, Desc: Boolean) :List<Record>{
            return if (Desc) {
                AppDatabase.instance.userDAO().getWaterBillDesc(needAccount)
            } else {
                AppDatabase.instance.userDAO().getWaterBillAsc(needAccount)
            }
        }

        //余额查询
        fun getAccountBalance(needAccount: String):Float{
            val income = getSumAmount(
                AppDatabase.instance.userDAO().getWaterBillIncome(
                    needAccount,
                    true
                )
            )
            val pay = getSumAmount(
                AppDatabase.instance.userDAO().getWaterBillIncome(
                    needAccount,
                    false
                )
            )
            return income - pay
        }

        //每日支出的数据包
        fun getDayPay(needAccount: String, needDate: Date):List<Record>{
            return selectData(
                needAccount = needAccount,
                startDate = needDate,
                endDate = needDate,
                income = false
            )
        }

        //每日收入的数据包
        fun getDayIncome(needAccount: String, needDate: Date):List<Record>{
            return selectData(
                needAccount = needAccount,
                startDate = needDate,
                endDate = needDate,
                income = true
            )
        }

        //每日收支
        fun getDayBalance(needAccount: String, needDate: Date):Float{
            return getSumAmount(getDayIncome(needAccount, needDate)) - getSumAmount(
                getDayPay(
                    needAccount,
                    needDate
                )
            )
        }

        //近一月支出数据包
        fun getMonthPay(needAccount: String, needCalendar: Calendar):List<Record> {
            val startDate = Date(needCalendar.timeInMillis)
            needCalendar.add(Calendar.MONTH, -1)
            val endDate = Date(needCalendar.timeInMillis)
            return selectData(
                needAccount = needAccount,
                startDate = startDate,
                endDate = endDate,
                income = false
            )
        }

        //近一月收入数据包
        fun getMonthIncome(needAccount: String, needCalendar: Calendar):List<Record> {
            val startDate = Date(needCalendar.timeInMillis)
            needCalendar.add(Calendar.MONTH, -1)
            val endDate = Date(needCalendar.timeInMillis)
            return selectData(
                needAccount = needAccount,
                startDate = startDate,
                endDate = endDate,
                income = true
            )
        }

        //近一月收支
        fun getMonthBalance(needAccount: String, needCalendar: Calendar):Float{
            return getSumAmount(getMonthIncome(needAccount, needCalendar))-getSumAmount(
                getMonthPay(
                    needAccount,
                    needCalendar
                )
            )
        }

        //近一年支出数据包
        fun getYearPay(needAccount: String, needCalendar: Calendar):List<Record> {
            val startDate = Date(needCalendar.timeInMillis)
            needCalendar.add(Calendar.YEAR, -1)
            val endDate = Date(needCalendar.timeInMillis)
            return selectData(
                needAccount = needAccount,
                startDate = startDate,
                endDate = endDate,
                income = false
            )
        }

        //近一年收入数据包
        fun getYearIncome(needAccount: String, needCalendar: Calendar):List<Record> {
            val startDate = Date(needCalendar.timeInMillis)
            needCalendar.add(Calendar.YEAR, -1)
            val endDate = Date(needCalendar.timeInMillis)
            return selectData(
                needAccount = needAccount,
                startDate = startDate,
                endDate = endDate,
                income = true
            )
        }

        //近一年收支
        fun getYearBalance(needAccount: String, needCalendar: Calendar):Float{
            return getSumAmount(getYearIncome(needAccount, needCalendar))-getSumAmount(
                getYearPay(
                    needAccount,
                    needCalendar
                )
            )
        }


        //---------------------------------------------------------------------------------------------------------------


        val textView = findViewById<TextView>(R.id.UserNametextView)
        textView.text = "***"//<-----------------这里传入当前账户名--------------------------------------------------------
        val searchCategory: MutableList<String> = mutableListOf()
        val searchSubCategory: MutableList<String> = mutableListOf()
        val searchMember:MutableList<String> = mutableListOf()
        val searchItem:MutableList<String> = mutableListOf()
        val searchMerchant:MutableList<String> = mutableListOf()
        val searchType:MutableList<String> = mutableListOf()
        val StatisticArray = arrayOf<String>("", "", "", "", "", "")
        fun InitStatisticSpinner(itemlist: List<String>, ID: Int, Index: Int) {
            var selectedSpinner = findViewById<Spinner>(ID)

            var selectedSpinnerAdapter: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, itemlist)
            selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectedSpinner.setAdapter(selectedSpinnerAdapter)

            selectedSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                //这个函数在单击时被调用
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    //这里是onclick功能的具体实现
                    //用于新建/修改Record
                    StatisticArray[Index] = adapterView.getItemAtPosition(i) as String
                    if (Index == CATEGORY_S_index) {
                        subcategoryStringListSpinner.clear()
                        for (subcategory in AppDatabase.instance.userDAO().getAllSubcategory(
                            StatisticArray[Index]
                        )) {
                            subcategoryStringListSpinner.add(subcategory.subcategory)
                        }
                        InitStatisticSpinner(
                            subcategoryStringListSpinner.toList(),
                            R.id.spinnerSubCategory,
                            SUBCATEGORY_S_index
                        )
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }
            })
        }

        InitStatisticSpinner(categoryStringList.toList(), R.id.spinnerCategory, CATEGORY_S_index)
        InitStatisticSpinner(
            subcategoryStringListSpinner.toList(),
            R.id.spinnerSubCategory,
            SUBCATEGORY_S_index
        )
        InitStatisticSpinner(memberStringList.toList(), R.id.spinnerMember, MEMBER_S_index)
        InitStatisticSpinner(itemStringList.toList(), R.id.spinnerItem, ITEM_S_index)
        InitStatisticSpinner(merchantStringList.toList(), R.id.spinnerMerchant, MERCHANT_S_index)
        InitStatisticSpinner(DEFAULT_TYPE_LIST, R.id.spinnerType, TYPE_S_index)

        //添加按钮
        button_s_add.setOnClickListener{
            //有重复！！取用的时候需要去重
            searchCategory.add(StatisticArray[CATEGORY_S_index])
            searchSubCategory.add(StatisticArray[SUBCATEGORY_S_index])
            searchMember.add(StatisticArray[MEMBER_S_index])
            searchItem.add(StatisticArray[ITEM_S_index])
            searchMerchant.add(StatisticArray[MERCHANT_S_index])
            searchType.add(StatisticArray[TYPE_S_index])
        }

        //去除重复
        fun removeDuplicate(list: MutableList<*>):MutableList<*>{
            for (i in 0 until list.size) {
                for (j in 0 until list.size) {
                    if(i!=j&& list[i] == list[j]) {
                        list.remove(list[j]);
                    }
                }
            }
            return list
        }

        /*
        //下一步按钮
        s1_next.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, "下一个页面"::class.java)
            startActivity(intent)
            finish()
        }*/
        }
    val timeend:MutableList<Int> = mutableListOf()
    val timestart:MutableList<Int> = mutableListOf()
    //timestart[0]:Year
    //timestart[1]:month
    //timestart[1]:day

    @SuppressLint("SetTextI18n")
    fun SelectEndTime(view: View) {
        Toast.makeText(this, "nmsl", Toast.LENGTH_LONG).show()
        var cal=Calendar.getInstance()
        var year=cal.get(Calendar.YEAR)      //获取年月日时分秒
        var month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        var day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            OnDateSetListener { arg0, year, month, day ->
                //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                timeend.add(year)
                timeend.add(month+1)
                timeend.add(day)
                Log.d("ggg", ""+timeend[2])
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()

    }
    fun SelectStartTime(view: View) {
        Toast.makeText(this, "asdfasdf", Toast.LENGTH_LONG).show()
        var cal=Calendar.getInstance()
        var year=cal.get(Calendar.YEAR)       //获取年月日时分秒
        var month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        var day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            OnDateSetListener { arg0, year, month, day ->
                //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                timestart.add(year)
                timestart.add(month+1)
                timestart.add(day)
                Log.d("ggg", ""+timestart[1])
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()
    }

}