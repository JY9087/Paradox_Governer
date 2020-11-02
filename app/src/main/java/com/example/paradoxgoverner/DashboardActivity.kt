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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.statistics.*
import java.util.*
import kotlin.properties.Delegates

class DashboardActivity : AppCompatActivity() {

    companion object {
        var instance: DashboardActivity by Delegates.notNull()
        fun instance() = instance
        var sourceDashFlag = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_dashboard)

        button4.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, statisticsActivity::class.java)
            startActivity(intent)
            finish()
        }

        //统计
        var acStr = "账户："
        var caStr = "一级分类："
        var subcaStr = "二级分类："
        var memStr = "成员："
        var merStr = "商家："
        var itStr = "项目："
        var tyStr = "类别："
        var startTimeStr = "开始时间："
        var endTimeStr = "结束时间："
        var totalAmount = "总金额："

        //下标从0开始
        var valuesSize = statisticsActivity.types.size-1
        if(valuesSize>=0){
            for(index in 0..valuesSize){
                when(statisticsActivity.types[index]){
                    "账户"->acStr += statisticsActivity.values[index] +" "
                    "一级分类" ->caStr += statisticsActivity.values[index]+" "
                    "二级分类" ->subcaStr += statisticsActivity.values[index]+" "
                    "成员" ->memStr += statisticsActivity.values[index]+" "
                    "商家" -> merStr += statisticsActivity.values[index]+" "
                    "项目" -> itStr += statisticsActivity.values[index]+" "
                    "类别" -> tyStr += statisticsActivity.values[index]+" "
                    "开始时间" -> startTimeStr += statisticsActivity.values[index]+" "
                    "结束时间" -> endTimeStr += statisticsActivity.values[index]+" "
                }
            }
        }


        if(statisticsActivity.searchFlag){
            var remainAmount = 0.0
            for(record in statisticsActivity.recordList2){
                if(record.income){
                    remainAmount += record.amount
                }
                else{
                    remainAmount -= record.amount
                }
            }
            val remainAmountString :String = String.format("%.2f",(remainAmount))
            totalAmount += remainAmountString
        }

        dashAccountText.text = acStr
        dashCategoryText.text = caStr
        dashSubcategoryText.text = subcaStr
        dashMemberText.text = memStr
        dashMerchantText.text = merStr
        dashItemText.text = itStr
        dashTypeText.text = tyStr
        dashStartTimeText.text = startTimeStr
        dashEndTimeText.text = endTimeStr
        dashTotalAmountText.text = totalAmount


        var bottomNavigatior = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigatior.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_dashboard -> {

                }
                R.id.navigation_graph -> {
                    val intent = Intent(this, GraphActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_personal -> {
                    val intent = Intent(this, PersonalActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
            }
            true
        })
        bottomNavigatior.selectedItemId = R.id.navigation_dashboard

        if(statisticsActivity.searchFlag){
            val DashboardList = findViewById<RecyclerView>(R.id.dashboard_recyeleview)
            DashboardList.layoutManager = LinearLayoutManager(this)
            var myadapter = ForecastListAdapter(statisticsActivity.recordList2)
            DashboardList.adapter = myadapter
            statisticsActivity.searchFlag = false

            var recyclertouchlistener = MainActivity.RecyclerTouchListener(
                this,
                DashboardList,
                object : ClickListener {
                    //单击事件  进入Record
                    override fun onClick(view: View?, position: Int) {
                        sourceDashFlag = true
                        //传递UID，由新Activity去进行查询
                        val intent =
                            Intent(MainActivity.instance, CreateNewItem::class.java).putExtra(
                                RECORD_UID, statisticsActivity.recordList2.get(position).uid
                            )
                        startActivity(intent)
                        finish()
                    }

                    override fun onLongClick(view: View?, position: Int) {
                    }
                }
            )
            //onClick
            DashboardList.addOnItemTouchListener(recyclertouchlistener)
        }

    }

    fun vieaAll(view : View){
        val intent = Intent()
        intent.setClass(this, ViewAllActivity::class.java)
        startActivity(intent)
        finish()
    }
}