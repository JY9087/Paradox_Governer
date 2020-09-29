package com.example.paradoxgoverner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.ContentView
import androidx.recyclerview.widget.RecyclerView

class CreateNewItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization_of_new_item)
    }

    fun CreateNewItem(view: View) {
        val editText = findViewById<EditText>(R.id.Description)
        val message = editText.text.toString()

        AppDatabase.instance.userDAO().insertAll(Record(0,message))

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}