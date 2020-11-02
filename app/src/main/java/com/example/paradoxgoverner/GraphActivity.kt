package com.example.paradoxgoverner

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.properties.Delegates

class GraphActivity : AppCompatActivity() {

    var startCalendar: Calendar = Calendar.getInstance()
    var endCalendar: Calendar = Calendar.getInstance()

    var startTimeFlag = false
    var endTimeFlag = false
    var itemNameID = 0

    companion object {
        var instance: GraphActivity by Delegates.notNull()
        fun instance() = instance
    }

    fun getSumByMemberin(member: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMember(member) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis){
                if(records.income){ tot += records.amount }
            }
        }
        return tot
    }

    fun getAllSumByMemin(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Memin = arrayListOf<Any>()
        for (mem in DAO.getAllMember()) {
            Memin.add(arrayOf(mem.member, getSumByMemberin(mem.member, StartTime, EndTime)))
        }
        return Memin.toTypedArray()
    }

    fun getSumByMemberout(member: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMember(member) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (!records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByMemout(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Memout = arrayListOf<Any>()
        for (mem in DAO.getAllMember()) {
            Memout.add(arrayOf(mem.member, getSumByMemberout(mem.member, StartTime, EndTime)))
        }
        return Memout.toTypedArray()
    }

    fun getSumByItemin(item: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByItem(item) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByItemin(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Itemin = arrayListOf<Any>()
        for (item in DAO.getAllItem()) {
            Itemin.add(arrayOf(item.item, getSumByItemin(item.item, StartTime, EndTime)))
        }
        return Itemin.toTypedArray()
    }

    fun getSumByItemout(item: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByItem(item) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (!records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByItemout(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Itemout = arrayListOf<Any>()
        for (item in DAO.getAllItem()) {
            Itemout.add(arrayOf(item.item, getSumByItemout(item.item, StartTime, EndTime)))
        }
        return Itemout.toTypedArray()
    }

    fun getSumByMerchantin(merchant: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMerchant(merchant) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByMerchantin(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Mercin = arrayListOf<Any>()
        for (merc in DAO.getAllMerchant()) {
            Mercin.add(arrayOf(merc.merchant, getSumByMerchantin(merc.merchant, StartTime, EndTime)))
        }
        return Mercin.toTypedArray()
    }

    fun getSumByMerchantout(merchant: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMerchant(merchant) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (!records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByMerchantout(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Mercout = arrayListOf<Any>()
        for (merc in DAO.getAllMerchant()) {
            Mercout.add(arrayOf(merc.merchant, getSumByMerchantout(merc.merchant, StartTime, EndTime)))
        }
        return Mercout.toTypedArray()
    }

    fun getSumByAccountin(acc: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByAccount(acc) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByAccountin(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Accin = arrayListOf<Any>()
        for (acco in DAO.getAllAccount()) {
            Accin.add(arrayOf(acco.account, getSumByAccountin(acco.account, StartTime, EndTime)))
        }
        return Accin.toTypedArray()
    }

    fun getSumByAccountout(acc: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByAccount(acc) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (!records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByAccountout(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Accout = arrayListOf<Any>()
        for (acco in DAO.getAllAccount()) {
            Accout.add(arrayOf(acco.account, getSumByAccountout(acco.account, StartTime, EndTime)))
        }
        return Accout.toTypedArray()
    }

    fun getSumByFCatout(Fcat: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCategory(Fcat) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (!records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByFcatout(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Fcatout = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            Fcatout.add(arrayOf(cat.category, getSumByFCatout(cat.category, StartTime, EndTime)))
        }
        return Fcatout.toTypedArray()
    }

    fun getSumByFCatin(Fcat: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCategory(Fcat) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByFcatin(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Fcatin = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            Fcatin.add(arrayOf(cat.category, getSumByFCatin(cat.category, StartTime, EndTime)))
        }
        return Fcatin.toTypedArray()
    }

    fun getSumBySCatout(Cat: String, Scat: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCatSubcategory(Cat, Scat) ) {
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (!records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByScatout(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Scatout = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            for (subcat in DAO.getAllSubcategory(cat.category)) {
                Scatout.add(arrayOf(subcat.category + ":" + subcat.subcategory, getSumBySCatout(subcat.category, subcat.subcategory, StartTime, EndTime)))
            }
        }
        return Scatout.toTypedArray()
    }

    fun getSumBySCatin(Cat: String, Scat: String, StartTime: Calendar, EndTime: Calendar) : Float {
        var tot = 0F
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCatSubcategory(Cat, Scat) ) {
            //假定收入为正，支出为负
            if(records.date.time >= StartTime.timeInMillis && records.date.time <= EndTime.timeInMillis) {
                if (records.income) {
                    tot += records.amount
                }
            }
        }
        return tot
    }

    fun getAllSumByScatin(StartTime: Calendar, EndTime: Calendar) : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        val Scatin = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            for (subcat in DAO.getAllSubcategory(cat.category)) {
                Scatin.add(arrayOf(subcat.category + ":" + subcat.subcategory, getSumBySCatin(subcat.category, subcat.subcategory, StartTime, EndTime)))
            }
        }
        return Scatin.toTypedArray()
    }

    fun selectStartTime(view: View) {

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)      //获取年月日时分秒
        val month = cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            DatePickerDialog.OnDateSetListener { arg0, year, month, day ->
                startCalendar.set(year, month, day, 0, 0, 0)
                startTimeFlag = true
                aaChartAdapt()
            }
        val dialog = DatePickerDialog(
            this,
            0,
            listener,
            year,
            month,
            day
        ) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()

    }

    fun selectEndTime(view: View) {

        val cal= Calendar.getInstance()
        val year=cal.get(Calendar.YEAR)      //获取年月日时分秒
        val month=cal.get(Calendar.MONTH)   //获取到的月份是从0开始计数
        val day=cal.get(Calendar.DAY_OF_MONTH)
        val listener =
            DatePickerDialog.OnDateSetListener { arg0, year, month, day ->
                endCalendar.set(year,month,day,23, 59, 59)
                endTimeFlag = true
                aaChartAdapt()
            }
        val dialog = DatePickerDialog(this, 0, listener, year, month, day) //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_graph)

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
                R.id.navigation_dashboard-> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_graph -> {}
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
        bottomNavigatior.selectedItemId = R.id.navigation_graph

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("一级分类支出")
            .backgroundColor("#ffffff")
            .stacking(AAChartStackingType.Percent)
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("支出")
                    .size("40%")
                    .innerSize("30%")
                    .borderWidth(0f)
                    .data(
                        getAllSumByFcatout(startCalendar, endCalendar)

                    )
            )
            )
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_gtype_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        var aaChartModel = AAChartModel()
        itemNameID = item.itemId
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
        when (item.itemId) {
            R.id.navigation_fcato -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("一级分类支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByFcatout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_fcati -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("一级分类收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByFcatin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_scati -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("二级分类收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByScatin(startCalendar, endCalendar)
                            )
                            )
                    )
            }
            R.id.navigation_scato -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("二级分类支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByScatout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_memin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("成员收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMemin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_memout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("成员支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMemout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_mercin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("商家收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMerchantin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_mercout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("商家支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMerchantout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_accin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("账户收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByAccountin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_accout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("账户支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByAccountout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_itemin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("项目收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByItemin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_itemout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("项目支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByItemout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            else -> super.onOptionsItemSelected(item)
        }
        aaChartView.aa_refreshChartWithChartModel(aaChartModel)
        return true
    }

    private fun aaChartAdapt(){
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        var aaChartModel = AAChartModel()
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
        when (itemNameID) {
            R.id.navigation_fcato -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("一级分类支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByFcatout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_fcati -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("一级分类收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByFcatin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_scati -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("二级分类收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByScatin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_scato -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("二级分类支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByScatout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_memin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("成员收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMemin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_memout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("成员支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMemout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_mercin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("商家收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMerchantin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_mercout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("商家支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByMerchantout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_accin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("账户收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByAccountin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_accout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("账户支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByAccountout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_itemin -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("项目收入")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("收入")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByItemin(startCalendar, endCalendar)
                            )
                    )
                    )
            }
            R.id.navigation_itemout -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("项目支出")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(
                                getAllSumByItemout(startCalendar, endCalendar)
                            )
                    )
                    )
            }
        }
        aaChartView.aa_refreshChartWithChartModel(aaChartModel)

    }

}