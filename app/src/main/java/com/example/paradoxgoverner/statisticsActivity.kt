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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class statisticsActivity : AppCompatActivity() {


    var searchCategory = mutableListOf<String>()
    var searchSubCategory = mutableListOf<String>()
    var searchMember = mutableListOf<String>()
    var searchItem = mutableListOf<String>()
    var searchMerchant = mutableListOf<String>()
    var searchType = mutableListOf<String>()
    var searchAccount = mutableListOf<String>()
    var memberStringList = mutableListOf<String>()
    var categoryStringList = mutableListOf<String>()
    var subcategoryStringList = mutableListOf<String>()
    var itemStringList = mutableListOf<String>()
    var merchantStringList = mutableListOf<String>()
    var accountStringList = mutableListOf<String>()
    private val startCalendar: Calendar = Calendar.getInstance()
    private val endCalendar: Calendar = Calendar.getInstance()



    var init_flag = false
    var startTimeFlag = false
    var endTimeFlag = false
    companion object {
        var instance: PersonalActivity by Delegates.notNull()
        fun instance() = instance
        var recordList2 :MutableList<Record> = mutableListOf()
        var searchFlag = true
        var types = mutableListOf<String>()
        var values = mutableListOf<String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)
        //-------------------------------------------------------------------------------------------<<<  UI
        //获取所有项
        types.clear()
        values.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (members in DAO.getAllMember()) {
            memberStringList.add(members.member)
        }
        for (category in DAO.getAllCategory()) {
            categoryStringList.add(category.category)
        }
        for (subcategory in AppDatabase.instance.userDAO().getAllSubcategory(DEFAULT_CATEGORY_LIST[0])) {
            subcategoryStringList.add(subcategory.subcategory)
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

        init_flag = false

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

    fun confirm(view : View){
        init_flag = true
        Toast.makeText(this@statisticsActivity, "可以开始添加条件", Toast.LENGTH_SHORT).show()
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
                    "开始时间"->startTimeFlag = false
                    "结束时间"->endTimeFlag = false
                }
                types.removeAt(position)
                values.removeAt(position)

                val conditionList = findViewById<RecyclerView>(R.id.ConditionRecycleView)
                conditionList.adapter = ConditionListAdapter(types.toList(),values.toList())
            })
            .setNegativeButton("取消", null)
            .show()
    }


    //确定按钮
    fun startSearch(view: View){
        searchFlag = true
        val DAO = AppDatabase.instance.userDAO()
        //空总算是处理好了
        if(searchAccount.size == 0){ searchAccount = accountStringList}
        //没有选择一级选项
        if(searchCategory.size == 0){
            searchCategory = categoryStringList
            for (subcategory in AppDatabase.instance.userDAO().getAllSubcategoryWithoutCategory()) {
                subcategoryStringList.add(subcategory.subcategory)
            }
            searchSubCategory = subcategoryStringList
        }

        //选了一级没选二级
        if(searchSubCategory.size == 0 && searchCategory.size != 0){
            var subStrList = mutableListOf<String>()
            var ninFlag = true
            for(category in searchCategory){
                //选了一级分类没选二级分类就选择全部一级分类
                subStrList.clear()
                ninFlag = true
                for (subcategory in AppDatabase.instance.userDAO().getAllSubcategory(category)) {
                    subStrList.add(subcategory.subcategory)
                }
                for(sub in searchSubCategory){
                    //查到了
                    if(subStrList.indexOf(sub) != -1){
                        ninFlag = false
                    }
                }
                //没查到
                if(ninFlag){
                    for(subStr in subStrList){
                        searchSubCategory.add(subStr)
                    }
                }
            }
        }



        if(searchMember.size == 0){ searchMember = memberStringList}
        if(searchMerchant.size == 0){ searchMerchant = merchantStringList}
        if(searchItem.size == 0){ searchItem = itemStringList}
        if(searchType.size == 0){
            for(types in DEFAULT_TYPE_LIST){
                searchType.add(types)
            }
        }
        //玄学
        if(!startTimeFlag){
            val cal= Calendar.getInstance()
            val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
            val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
            val day=cal.get(Calendar.DAY_OF_MONTH)
            startCalendar.set(year-100,month,day,0,0,0)
        }
        if(!endTimeFlag){
            val cal= Calendar.getInstance()
            val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
            val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
            val day=cal.get(Calendar.DAY_OF_MONTH)
            endCalendar.set(year+100,month,day,0,0,0)
        }
        getStatistics(false,searchAccount.toList(),searchMember.toList(),searchCategory.toList(),
            searchSubCategory.toList(),searchItem.toList(),searchMerchant.toList(),
            searchType.toList(),startCalendar,endCalendar)
        val intent = Intent(this, DashboardActivity::class.java)
        if (MainActivity.versionFlag) {
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
        }
        startActivity(intent)
        finish()
    }

    //现在的问题是在init时会点击
    //无法点击已选项
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
                if (init_flag) {
                    Toast.makeText(this@statisticsActivity, "添加条件", Toast.LENGTH_SHORT).show()
                    if (Index == CATEGORY_INDEX) {
                        subcategoryStringList.clear()
                        for (subcategory in AppDatabase.instance.userDAO()
                            .getAllSubcategory(adapterView.getItemAtPosition(i) as String)) {
                            subcategoryStringList.add(subcategory.subcategory)
                        }
                        initStatisticSpinner(
                            subcategoryStringList.toList(),
                            R.id.spinnerSubCategory,
                            SUBCATEGORY_INDEX
                        )
                    }

                    when (Index) {
                        ACCOUNT_INDEX -> searchAccount.add(adapterView.getItemAtPosition(i) as String)
                        CATEGORY_INDEX -> searchCategory.add(adapterView.getItemAtPosition(i) as String)
                        SUBCATEGORY_INDEX -> searchSubCategory.add(adapterView.getItemAtPosition(i) as String)
                        MEMBER_INDEX -> searchMember.add(adapterView.getItemAtPosition(i) as String)
                        ITEM_INDEX -> searchItem.add(adapterView.getItemAtPosition(i) as String)
                        MERCHANT_INDEX -> searchMerchant.add(adapterView.getItemAtPosition(i) as String)
                        TYPE_INDEX -> searchType.add(adapterView.getItemAtPosition(i) as String)
                    }

                    var tmp_type = ""
                    when (Index) {
                        MEMBER_INDEX -> tmp_type = "成员"
                        CATEGORY_INDEX -> tmp_type = "一级分类"
                        SUBCATEGORY_INDEX -> tmp_type = "二级分类"
                        MERCHANT_INDEX -> tmp_type = "商家"
                        ITEM_INDEX -> tmp_type = "项目"
                        ACCOUNT_INDEX -> tmp_type = "账户"
                        TYPE_INDEX -> tmp_type = "类别"
                    }
                    types.add(tmp_type)
                    values.add(adapterView.getItemAtPosition(i) as String)
                    val conditionList = findViewById<RecyclerView>(R.id.ConditionRecycleView)
                    conditionList.adapter = ConditionListAdapter(types.toList(), values.toList())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
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
        needAccount: List<String> = accountStringList.toList(),
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = this.subcategoryStringList.toList(),
        needItem: List<String> = itemStringList.toList(),
        needMerchant: List<String> = merchantStringList.toList(),
        needType : List<String> = DEFAULT_TYPE_LIST,
        start:Calendar = startCalendar,
        end:Calendar = endCalendar
    ):List<Record>{
        recordList2.clear()
        val recordList1 = AppDatabase.instance.userDAO().selectDAO(needMember,needCategory,needSubcategory,needAccount,needType,needMerchant,needItem,true)
        val recordList3 = AppDatabase.instance.userDAO().selectDAO(needMember,needCategory,needSubcategory,needAccount,needType,needMerchant,needItem,false)
        for (record in recordList1){
            if(record.date.time >= startCalendar.timeInMillis && record.date.time <= endCalendar.timeInMillis){
                recordList2.add(record)
            }
        }
        for (record in recordList3){
            if(record.date.time >= startCalendar.timeInMillis && record.date.time <= endCalendar.timeInMillis){
                recordList2.add(record)
            }
        }
        return recordList2
        //返回空不会崩溃。Adapt可以Adapt空
    }


    //巧妙的设计
    fun getYearStatistics(
        needIncome:Boolean = true,
        needAccount: List<String> = accountStringList.toList(),
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = this.subcategoryStringList.toList(),
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
        needAccount: List<String> = accountStringList.toList(),
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = this.subcategoryStringList.toList(),
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
        needAccount: List<String> = accountStringList.toList(),
        needMember:List<String> = memberStringList.toList(),
        needCategory: List<String> = categoryStringList.toList(),
        needSubcategory: List<String> = this.subcategoryStringList.toList(),
        needItem: List<String> = itemStringList.toList(),
        needMerchant: List<String> = merchantStringList.toList(),
        needType : List<String> = DEFAULT_TYPE_LIST,
        needCalendar:Calendar = Calendar.getInstance()
    ):List<Record>{
        return getStatistics(needIncome, needAccount, needMember, needCategory, needSubcategory, needItem, needMerchant, needType,needCalendar,needCalendar)
    }


    //通过日历得到的两个Calendar,按按钮后精确到日

    fun SelectStartTime(view: View) {
        val cal= Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
        val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            DatePickerDialog.OnDateSetListener { arg0, year, month, day ->
                startCalendar.set(year,month,day,0, 0, 0)
                startTimeFlag =true
                var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
                val startDate = Date(startCalendar.timeInMillis)
                //indexOf()返回-1 when not found
                if(types.indexOf("开始时间") == -1){
                    types.add("开始时间")
                    values.add(sdf.format(startDate))
                }
                else{
                    values[types.indexOf("开始时间")] = sdf.format(startDate)
                }

                val conditionList = findViewById<RecyclerView>(R.id.ConditionRecycleView)
                conditionList.adapter = ConditionListAdapter(types.toList(), values.toList())
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
                endCalendar.set(year,month,day,23, 59, 59)
                endTimeFlag = true
                var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
                var endDate = Date(endCalendar.timeInMillis)
                if(types.indexOf("结束时间") == -1){
                    types.add("结束时间")
                    values.add(sdf.format(endDate))
                }
                else{
                    values[types.indexOf("结束时间")] = sdf.format(endDate)
                }
                val conditionList = findViewById<RecyclerView>(R.id.ConditionRecycleView)
                conditionList.adapter = ConditionListAdapter(types.toList(), values.toList())
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()

    }
}

