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

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_gtype_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        var aaChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("支出分类统计")
            .backgroundColor("#ffffff")
            .stacking(AAChartStackingType.Percent)
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("支出")
                    .size("50%")
                    .innerSize("30%")
                    .borderWidth(0f)
                    .data(arrayOf(
                        arrayOf("餐饮", 100),
                        arrayOf("出行", 3),
                        arrayOf("购物", 100),
                        arrayOf("无", 0)
                        //TODO: 数据改为实时获取的数据
                    )
                    )
            )
            )
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
        when (item.itemId) {
            R.id.navigation_catagory -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("支出分类统计")
                    .backgroundColor("#ffffff")
                    .stacking(AAChartStackingType.Percent)
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .name("支出")
                            .size("40%")
                            .innerSize("30%")
                            .borderWidth(0f)
                            .data(arrayOf(
                                arrayOf("餐饮", 100),
                                arrayOf("出行", 3),
                                arrayOf("购物", 100),
                                arrayOf("无", 0)
                                //TODO: 数据改为实时获取的数据
                            )
                            )
                    )
                    )
            }
            R.id.navigation_water_bill -> {
                aaChartModel = AAChartModel()
                    .chartType(AAChartType.Line)
                    .categories(
                        arrayOf(
                            "7天前", "6天前", "5天前", "4天前", "3天前", "2天前",
                            "1天前", "今天"
                        )
                    )
                    .yAxisTitle("余额/元")
                    .title("余额流水统计")
                    .backgroundColor("#ffffff")
                    .dataLabelsEnabled(true)
                    .series(arrayOf(
                        AASeriesElement()
                            .data(arrayOf(12, 23, 11, 22, 55, 40, 20, 5))
                                //TODO: 数据改为实时获取的数据
                            )
                    )
            }
            else -> super.onOptionsItemSelected(item)
        }
        aaChartView.aa_refreshChartWithChartModel(aaChartModel)
        return true
    }

}