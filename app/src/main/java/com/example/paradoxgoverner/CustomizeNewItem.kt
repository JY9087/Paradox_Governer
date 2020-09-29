package com.example.paradoxgoverner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLogTags
import android.view.View
import android.widget.EditText
import androidx.annotation.ContentView
import androidx.recyclerview.widget.RecyclerView

class CustomizeNewItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization_of_new_item)
    }

    fun CustomizeNewItem(view: View) {
        val editText = findViewById<EditText>(R.id.Description)
        val message = editText.text.toString()

        //直接在数据库中添加
        //uid不该有值的。但我不会autoGenerate，所以先用随机数
        //这里insert没有成功
        //AppDatabase.instance.userDAO().insertAll(Record((0 until 10000000).random(),message))

        val intent = Intent(this, MainActivity::class.java)
            .putExtra(EXTRA_MESSAGE,message)
            .putExtra(OPERATION_MESSAGE, OPERATION_DESCRIPTION)
        startActivity(intent)
    }
}