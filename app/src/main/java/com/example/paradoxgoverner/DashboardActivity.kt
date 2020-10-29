package com.example.paradoxgoverner

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.statistics.*
import java.util.*
import kotlin.properties.Delegates

class DashboardActivity : AppCompatActivity() {

    companion object {
        var instance: DashboardActivity by Delegates.notNull()
        fun instance() = instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)



        button4.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, statisticsActivity::class.java)
            startActivity(intent)
            finish()

        }



        var bottomNavigatior = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigatior.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_dashboard -> {

                }
                R.id.navigation_graph -> {
                    val intent = Intent(this, GraphActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_personal -> {
                    val intent = Intent(this, PersonalActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
            }
            true
        })
        bottomNavigatior.selectedItemId = R.id.navigation_dashboard
    }
}
        /*//-------------------------------------------------------------------------------------------<<<  UI
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
        //-------------------------------------------------<<通过Spinner得到的Set，要使用的话请用 .toList() 转为List
        val searchCategory: MutableSet<String> = mutableSetOf()
        val searchSubCategory: MutableSet<String> = mutableSetOf()
        val searchMember:MutableSet<String> = mutableSetOf()
        val searchItem:MutableSet<String> = mutableSetOf()
        val searchMerchant:MutableSet<String> = mutableSetOf()
        val searchType:MutableSet<String> = mutableSetOf()
        val searchAccount:MutableSet<String> = mutableSetOf()
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

        //转到图表按钮，若有其他可用方式可用删掉
        //这里我用来debug
        s1_next.setOnClickListener {

        }
    }
    //通过日历得到的两个Calendar
    private val startCalendar: Calendar = Calendar.getInstance()
    private val endCalendar: Calendar = Calendar.getInstance()
    fun SelectStartTime(view: View) {
        val cal=Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
        val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            DatePickerDialog.OnDateSetListener { arg0, year, month, day ->
                startCalendar.set(year,month,day,0, 0, 0)
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()
    }
    fun SelectEndTime(view: View) {
        val cal=Calendar.getInstance()
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

    //统计函数
    fun getStatistics(
        needAccount: List<String>
    ):List<Record>{

    }*/