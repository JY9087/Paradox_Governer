package com.example.paradoxgoverner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class GraphActivity : AppCompatActivity() {

    companion object {
        var instance: GraphActivity by Delegates.notNull()
        fun instance() = instance
    }

    fun getSumByMemberin(member: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMember(member) ) {
            if(records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByMemin() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Memin = arrayListOf<Any>()
        for (mem in DAO.getAllMember()) {
            Memin.add(arrayOf(mem.member, getSumByMemberin(mem.member)))
        }
        return Memin.toTypedArray()
    }

    fun getSumByMemberout(member: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMember(member) ) {
            if(!records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByMemout() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Memout = arrayListOf<Any>()
        for (mem in DAO.getAllMember()) {
            Memout.add(arrayOf(mem.member, getSumByMemberout(mem.member)))
        }
        return Memout.toTypedArray()
    }

    fun getSumByItemin(item: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByItem(item) ) {
            if(records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByItemin() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Itemin = arrayListOf<Any>()
        for (item in DAO.getAllItem()) {
            Itemin.add(arrayOf(item.item, getSumByItemin(item.item)))
        }
        return Itemin.toTypedArray()
    }

    fun getSumByItemout(item: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByItem(item) ) {
            if(!records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByItemout() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Itemout = arrayListOf<Any>()
        for (item in DAO.getAllItem()) {
            Itemout.add(arrayOf(item.item, getSumByItemout(item.item)))
        }
        return Itemout.toTypedArray()
    }

    fun getSumByMerchantin(merchant: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMerchant(merchant) ) {
            if(records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByMerchantin() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Mercin = arrayListOf<Any>()
        for (merc in DAO.getAllMerchant()) {
            Mercin.add(arrayOf(merc.merchant, getSumByMerchantin(merc.merchant)))
        }
        return Mercin.toTypedArray()
    }

    fun getSumByMerchantout(merchant: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByMerchant(merchant) ) {
            if(!records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByMerchantout() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Mercout = arrayListOf<Any>()
        for (merc in DAO.getAllMerchant()) {
            Mercout.add(arrayOf(merc.merchant, getSumByMerchantout(merc.merchant)))
        }
        return Mercout.toTypedArray()
    }

    fun getSumByAccountin(acc: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByAccount(acc) ) {
            if(records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByAccountin() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Accin = arrayListOf<Any>()
        for (acco in DAO.getAllAccount()) {
            Accin.add(arrayOf(acco.account, getSumByAccountin(acco.account)))
        }
        return Accin.toTypedArray()
    }

    fun getSumByAccountout(acc: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByAccount(acc) ) {
            if(!records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByAccountout() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Accout = arrayListOf<Any>()
        for (acco in DAO.getAllAccount()) {
            Accout.add(arrayOf(acco.account, getSumByAccountout(acco.account)))
        }
        return Accout.toTypedArray()
    }

    fun getSumByFCatout(Fcat: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCategory(Fcat) ) {
            if(!records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByFcatout() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Fcatout = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            Fcatout.add(arrayOf(cat.category, getSumByFCatout(cat.category)))
        }
        return Fcatout.toTypedArray()
    }

    fun getSumByFCatin(Fcat: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCategory(Fcat) ) {
            //假定收入为正，支出为负
            if(records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByFcatin() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Fcatin = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            Fcatin.add(arrayOf(cat.category, getSumByFCatin(cat.category)))
        }
        return Fcatin.toTypedArray()
    }

    fun getSumBySCatout(Cat: String, Scat: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCatSubcategory(Cat, Scat) ) {
            if(!records.income){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByScatout() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Scatout = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            for (subcat in DAO.getAllSubcategory(cat.category)) {
                Scatout.add(arrayOf(subcat.category + ":" + subcat.subcategory, getSumBySCatout(subcat.category, subcat.subcategory)))
            }
        }
        return Scatout.toTypedArray()
    }

    fun getSumBySCatin(Cat: String, Scat: String) : Double {
        var tot = 0.0
        val DAO = AppDatabase.instance.userDAO()
        for ( records in DAO.findRecordByCatSubcategory(Cat, Scat) ) {
            //假定收入为正，支出为负
            if(records.income ){ tot += records.amount }
        }
        return tot
    }

    fun getAllSumByScatin() : Array<Any> {
        val DAO = AppDatabase.instance.userDAO()
        var Scatin = arrayListOf<Any>()
        for (cat in DAO.getAllCategory()) {
            for (subcat in DAO.getAllSubcategory(cat.category)) {
                Scatin.add(arrayOf(subcat.category + ":" + subcat.subcategory, getSumBySCatin(subcat.category, subcat.subcategory)))
            }
        }
        return Scatin.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        var bottomNavigatior = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigatior.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                }
                R.id.navigation_dashboard-> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                }
                R.id.navigation_graph -> {}
                R.id.navigation_personal -> {
                    val intent = Intent(this, PersonalActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
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
                        getAllSumByFcatout()
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
                                getAllSumByFcatout()
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
                                getAllSumByFcatin()
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
                                getAllSumByScatin()
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
                                getAllSumByScatout()
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
                                getAllSumByMemin()
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
                                getAllSumByMemout()
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
                                getAllSumByMerchantin()
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
                                getAllSumByMerchantout()
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
                                getAllSumByAccountin()
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
                                getAllSumByAccountout()
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
                                getAllSumByItemin()
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
                                getAllSumByItemout()
                            )
                    )
                    )
            }
            else -> super.onOptionsItemSelected(item)
        }
        aaChartView.aa_refreshChartWithChartModel(aaChartModel)
        return true
    }

}