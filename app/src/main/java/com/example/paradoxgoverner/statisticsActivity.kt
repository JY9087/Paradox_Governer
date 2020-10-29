package com.example.paradoxgoverner

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.statistics.*
import java.util.*

class statisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)
        //-------------------------------------------------------------------------------------------<<<  UI
        //获取所有项
        val memberStringList = mutableListOf<String>()
        val categoryStringList = mutableListOf<String>()
        val subcategoryStringList = mutableListOf<String>()
        val itemStringList = mutableListOf<String>()
        val merchantStringList = mutableListOf<String>()
        val accountStringList = mutableListOf<String>()

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
        for (item in AppDatabase.instance.userDAO().getAllItem()) {
            itemStringList.add(item.item)
        }
        for (merchant in AppDatabase.instance.userDAO().getAllMerchant()) {
            merchantStringList.add(merchant.merchant)
        }
        for (account in AppDatabase.instance.userDAO().getAllAccount()){
            accountStringList.add(account.account)
        }

        //-------------------------------------------------<<通过Spinner得到的Set，要使用的话请用 .toList() 转为List,可能需要转为全局变量
        val searchCategory: MutableSet<String> = mutableSetOf()
        val searchSubCategory: MutableSet<String> = mutableSetOf()
        val searchMember:MutableSet<String> = mutableSetOf()
        val searchItem:MutableSet<String> = mutableSetOf()
        val searchMerchant:MutableSet<String> = mutableSetOf()
        val searchType:MutableSet<String> = mutableSetOf()
        val searchAccount:MutableSet<String> = mutableSetOf()

        //---------------------------------------------------------------------
        val statisticArray = arrayOf<String>("", "", "", "", "", "","")
        val subcategoryStringListSpinner = mutableListOf<String>()
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
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })
        }
        //设定Spinner
        initStatisticSpinner(categoryStringList.toList(), R.id.spinnerCategory, CATEGORY_INDEX)
        initStatisticSpinner(subcategoryStringList.toList(), R.id.spinnerSubCategory, SUBCATEGORY_INDEX)
        initStatisticSpinner(memberStringList.toList(), R.id.spinnerMember, MEMBER_INDEX)
        initStatisticSpinner(itemStringList.toList(), R.id.spinnerItem, ITEM_INDEX)
        initStatisticSpinner(merchantStringList.toList(), R.id.spinnerMerchant, MERCHANT_INDEX)
        initStatisticSpinner(DEFAULT_TYPE_LIST, R.id.spinnerType, TYPE_INDEX)
        initStatisticSpinner(accountStringList,R.id.spinnerAccount, ACCOUNT_INDEX)
        //添加按钮
        button_s_add.setOnClickListener{
            searchAccount.add(statisticArray[ACCOUNT_INDEX])
            searchCategory.add(statisticArray[CATEGORY_INDEX])
            searchSubCategory.add(statisticArray[SUBCATEGORY_INDEX])
            searchMember.add(statisticArray[MEMBER_INDEX])
            searchItem.add(statisticArray[ITEM_INDEX])
            searchMerchant.add((statisticArray[MERCHANT_INDEX]))
            searchType.add(statisticArray[TYPE_INDEX])
        }

        fun calendar_to_string(calendar:Calendar):String{
            val A=calendar.get(Calendar.YEAR).toString()
            val B=(calendar.get(Calendar.MONTH)+1).toString()
            val C=calendar.get(Calendar.DAY_OF_MONTH).toString()
            return "$A-$B-$C"
        }

        //-------------------------------------供调用的统计函数------------------------------------
        //Calendar精确到天即可
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
            val recordList2 :MutableList<Record> = mutableListOf()
            for (record in recordList1){
                if(record.date.toString() >= calendar_to_string(start) && record.date.toString() <= calendar_to_string(end)){
                    recordList2.add(record)
                }
            }
            return recordList2
            //返回值有可能空，得到后先确认其是否为空，防止崩溃
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

        //输入日期，返回当日数据（精确到日即可）
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

        //输入Calendar，返回当月数据（精确到月即可）
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

        //输入Calendar，返回当年数据（精确到年即可）
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

        //返回按钮
        //这里我用来debug
        s1_next.setOnClickListener {
            //val s=getSumAmount(getYearStatistics())
            //Toast.makeText(this, s.toString(), Toast.LENGTH_LONG).show()
        }

    }
    //通过日历得到的两个Calendar,按按钮后精确到日
    private val startCalendar: Calendar = Calendar.getInstance()
    private val endCalendar: Calendar = Calendar.getInstance()
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

