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
    }
}