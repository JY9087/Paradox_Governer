package com.example.paradoxgoverner

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.properties.Delegates

class statisticsActivity : AppCompatActivity() {

    val statisticArray = arrayOf<String>("", "", "", "", "", "","")
    var subcategoryStringListSpinner = mutableListOf<String>()
    var searchCategory: MutableSet<String> = mutableSetOf()
    var searchSubCategory: MutableSet<String> = mutableSetOf()
    var searchMember:MutableSet<String> = mutableSetOf()
    var searchItem:MutableSet<String> = mutableSetOf()
    var searchMerchant:MutableSet<String> = mutableSetOf()
    var searchType:MutableSet<String> = mutableSetOf()
    var searchAccount:MutableSet<String> = mutableSetOf()
    var memberStringList = mutableListOf<String>()
    var categoryStringList = mutableListOf<String>()
    var subcategoryStringList = mutableListOf<String>()
    var itemStringList = mutableListOf<String>()
    var merchantStringList = mutableListOf<String>()
    var accountStringList = mutableListOf<String>()
    private val startCalendar: Calendar = Calendar.getInstance()
    private val endCalendar: Calendar = Calendar.getInstance()


    var types = mutableListOf<String>()
    var values = mutableListOf<String>()


    companion object {
        var instance: PersonalActivity by Delegates.notNull()
        fun instance() = instance
        var recordList2 :MutableList<Record> = mutableListOf()
        var searchFlag = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)
        //-------------------------------------------------------------------------------------------<<<  UI
        //获取所有项

        val DAO = AppDatabase.instance.userDAO()
        for (members in DAO.getAllMember()) {
            memberStringList.add(members.member)
        }
        for (category in DAO.getAllCategory()) {
            categoryStringList.add(category.category)
        }
        for (category in categoryStringList) {
            for (subcategory in DAO.getAllSubcategory(category))
            {
                subcategoryStringList.add(subcategory.subcategory)
            }
        }
        for (item in DAO.getAllItem()) {
            itemStringList.add(item.item)
        }
        for (merchant in DAO.getAllMerchant()) {
            merchantStringList.add(merchant.merchant)
        }
        for (account in DAO.getAllAccount()){
            accountStringList.add(account.account)
        }


        //设定Spinner
        initStatisticSpinner(categoryStringList.toList(), R.id.spinnerCategory, CATEGORY_INDEX)
        initStatisticSpinner(subcategoryStringList.toList(), R.id.spinnerSubCategory, SUBCATEGORY_INDEX)
        initStatisticSpinner(memberStringList.toList(), R.id.spinnerMember, MEMBER_INDEX)
        initStatisticSpinner(itemStringList.toList(), R.id.spinnerItem, ITEM_INDEX)
        initStatisticSpinner(merchantStringList.toList(), R.id.spinnerMerchant, MERCHANT_INDEX)
        initStatisticSpinner(DEFAULT_TYPE_LIST, R.id.spinnerType, TYPE_INDEX)
        initStatisticSpinner(accountStringList,R.id.spinnerAccount, ACCOUNT_INDEX)


        //RecycleView
        val conditionList = findViewById<RecyclerView>(R.id.ConditionRecycleView)
        conditionList.layoutManager = LinearLayoutManager(this)
        var myadapter = ConditionListAdapter(types.toList(),values.toList())
        conditionList.adapter = myadapter

        var recyclertouchlistener = MainActivity.RecyclerTouchListener(
            this,
            conditionList,
            object : ClickListener {
                //单击事件
                override fun onClick(view: View?, position: Int) {
                }

                override fun onLongClick(view: View?, position: Int) {
                    wantToDelete(position)
                }
            }
        )
        //onClick
        conditionList.addOnItemTouchListener(recyclertouchlistener)


    }

    fun wantToDelete(position : Int){
        AlertDialog.Builder(this)
            .setTitle("确认删除该条件？")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                when(types[position]){
                    "账户"-> searchAccount.remove(values[position])
                    "一级分类"-> searchCategory.remove(values[position])
                    "二级分类"-> searchSubCategory.remove(values[position])
                    "成员"-> searchMember.remove(values[position])
                    "项目"-> searchItem.remove(values[position])
                    "商家"-> searchMerchant.remove(values[position])
                    "类型"-> searchType.remove(values[position])
                }
                types.removeAt(position)
                values.removeAt(position)

            })
            .setNegativeButton("取消", null)
            .show()
    }

    //确定按钮
    fun startSearch(view: View){
        searchFlag = true
        val intent = Intent(this, MainActivity::class.java)
        if (MainActivity.versionFlag) {
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
        }
        startActivity(intent)
        finish()
    }

    //添加按钮
    fun addSearchString(view : View){
        recordList2.clear()
        getStatistics(true,searchAccount.toList(),searchMember.toList(),searchCategory.toList(),searchSubCategory.toList(),searchItem.toList(),searchMerchant.toList(),searchType.toList(),startCalendar,endCalendar)
    }


    fun calendar_to_string(calendar:Calendar):String{
        val A=calendar.get(Calendar.YEAR).toString()
        val B=(calendar.get(Calendar.MONTH)+1).toString()
        val C=calendar.get(Calendar.DAY_OF_MONTH).toString()
        return "$A-$B-$C"
    }

    //给定Record，求其Amount之和
    fun getSumAmount(SelectList: List<Record>):Float{
        var sumAmount = 0F
        for (item in SelectList)
        {
            sumAmount += item.amount
        }
        return sumAmount
    }

    fun getStatistics(
        needIncome:Boolean = true,
        needAccount: List<String> = accountStringList.toList() ,
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = subcategoryStringList.toList(),
        needItem: List<String> = itemStringList.toList(),
        needMerchant: List<String> = merchantStringList.toList(),
        needType : List<String> = DEFAULT_TYPE_LIST,
        start:Calendar = startCalendar,
        end:Calendar = endCalendar
    ):List<Record>{
        val recordList1 = AppDatabase.instance.userDAO().selectDAO(needMember,needCategory,needSubcategory,needAccount,needType,needMerchant,needItem,needIncome)

        for (record in recordList1){
            //String可以比较？这么厉害吗
            if(record.date.toString() >= calendar_to_string(start) && record.date.toString() <= calendar_to_string(end)){
                recordList2.add(record)
            }
        }
        return recordList2
        //返回值有可能空，得到后先确认其是否为空，防止崩溃
        //不会崩溃。Adapt可以Adapt空
    }

    //巧妙的设计
    fun getYearStatistics(
        needIncome:Boolean = true,
        needAccount: List<String> = accountStringList.toList() ,
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = subcategoryStringList.toList(),
        needItem: List<String> = itemStringList.toList(),
        needMerchant: List<String> = merchantStringList.toList(),
        needType : List<String> = DEFAULT_TYPE_LIST,
        needCalendar:Calendar = Calendar.getInstance()
    ):List<Record>{
        val year = needCalendar.get(Calendar.YEAR)
        val calendar1:Calendar = Calendar.getInstance()
        val calendar2:Calendar = Calendar.getInstance()
        calendar1.set(year,0,1)
        calendar2.set(year,11,31)
        return getStatistics(needIncome, needAccount, needMember, needCategory, needSubcategory, needItem, needMerchant,needType,calendar1,calendar2)
    }

    fun getMonthStatistics(
        needIncome:Boolean = true,
        needAccount: List<String> = accountStringList.toList() ,
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = subcategoryStringList.toList(),
        needItem: List<String> = itemStringList.toList(),
        needMerchant: List<String> = merchantStringList.toList(),
        needType : List<String> = DEFAULT_TYPE_LIST,
        needCalendar:Calendar = Calendar.getInstance()
    ):List<Record>{
        val year = needCalendar.get(Calendar.YEAR)
        val month = needCalendar.get(Calendar.MONTH)
        val calendar1:Calendar = Calendar.getInstance()
        val calendar2:Calendar = Calendar.getInstance()
        calendar1.set(year,month,1)
        calendar2.set(year,month+1,0)
        return getStatistics(needIncome, needAccount, needMember, needCategory, needSubcategory, needItem, needMerchant,needType,calendar1,calendar2)
    }

    fun getDayStatistics(
        needIncome:Boolean = true,
        needAccount: List<String> = accountStringList.toList() ,
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = subcategoryStringList.toList(),
        needItem: List<String> = itemStringList.toList(),
        needMerchant: List<String> = merchantStringList.toList(),
        needType : List<String> = DEFAULT_TYPE_LIST,
        needCalendar:Calendar = Calendar.getInstance()
    ):List<Record>{
        return getStatistics(needIncome, needAccount, needMember, needCategory, needSubcategory, needItem, needMerchant, needType,needCalendar,needCalendar)
    }

    fun initStatisticSpinner(itemList: List<String>, ID: Int, Index: Int) {
        var selectedSpinner = findViewById<Spinner>(ID)

        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, itemList)
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
                statisticArray[Index] = adapterView.getItemAtPosition(i) as String
                if (Index == CATEGORY_INDEX) {
                    subcategoryStringListSpinner.clear()
                    for (subcategory in AppDatabase.instance.userDAO().getAllSubcategory(
                        statisticArray[Index]
                    )) {
                        subcategoryStringListSpinner.add(subcategory.subcategory)
                    }
                    initStatisticSpinner(
                        subcategoryStringListSpinner.toList(),
                        R.id.spinnerSubCategory,
                        SUBCATEGORY_INDEX
                    )
                }
                when(Index){
                    ACCOUNT_INDEX-> searchAccount.add(statisticArray[ACCOUNT_INDEX])
                    CATEGORY_INDEX-> searchCategory.add(statisticArray[CATEGORY_INDEX])
                    SUBCATEGORY_INDEX-> searchSubCategory.add(statisticArray[SUBCATEGORY_INDEX])
                    MEMBER_INDEX-> searchMember.add(statisticArray[MEMBER_INDEX])
                    ITEM_INDEX-> searchItem.add(statisticArray[ITEM_INDEX])
                    MERCHANT_INDEX-> searchMerchant.add((statisticArray[MERCHANT_INDEX]))
                    TYPE_INDEX-> searchType.add(statisticArray[TYPE_INDEX])
                }


                var tmp_type = ""
                when(Index){
                    MEMBER_INDEX-> tmp_type = "成员"
                    CATEGORY_INDEX-> tmp_type = "一级分类"
                    SUBCATEGORY_INDEX-> tmp_type = "二级分类"
                    MERCHANT_INDEX-> tmp_type = "商家"
                    ITEM_INDEX-> tmp_type = "项目"
                    ACCOUNT_INDEX-> tmp_type = "账户"
                    TYPE_INDEX-> tmp_type = "类别"
                }
                types.add(tmp_type)
                values.add(adapterView.getItemAtPosition(i) as String)
                val conditionList = findViewById<RecyclerView>(R.id.ConditionRecycleView)
                conditionList.adapter = ConditionListAdapter(types.toList(),values.toList())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
    }
    //通过日历得到的两个Calendar,按按钮后精确到日

    fun SelectStartTime(view: View) {
        val cal= Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
        val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            DatePickerDialog.OnDateSetListener { arg0, year, month, day ->
                startCalendar.set(year,month,day,23, 59, 59)
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()
    }
    fun SelectEndTime(view: View) {
        val cal= Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
        val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            DatePickerDialog.OnDateSetListener { arg0, year, month, day ->
                //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                endCalendar.set(year,month,day,0, 0, 0)
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()
    }
}

