package com.example.paradoxgoverner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    //静态内部类：伴生对象
    //为什么这里要多一个instance()函数？
    companion object {
        var instance:  MainActivity by Delegates.notNull()
        fun instance() = instance
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Room
        instance = this
        val DAO : UserDAO = AppDatabase.instance.userDAO()


        //处理CustomizeNewItem事件
        if(OPERATION_DESCRIPTION == OPERATION_DESCRIPTION)
        {
            ConfirmNewItem();
        }




        //RecycleView
        var forecastList = findViewById<RecyclerView>(R.id.forecast)
        forecastList.layoutManager = LinearLayoutManager(this)
        forecastList.adapter = ForecastListAdapter(DAO.getAll())

    }

    //override fun on

    fun CreateNewItem(view:View)
    {
        val intent = Intent(this, CustomizeNewItem::class.java)
        startActivity(intent)
    }

    fun ConfirmNewItem()
    {
        val DAO : UserDAO = AppDatabase.instance.userDAO()
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        DAO.insertAll(Record((0 until 10000000).random(), message))
    }
}