package com.example.paradoxgoverner


import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_customization_of_new_item.*
import java.sql.Date
import java.sql.Time
import java.util.*


class CreateNewItem : AppCompatActivity() {

    lateinit var memberstring : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization_of_new_item)

        var timepicker : TimePicker
        var minute : Int
        var hourofday : Int
        var calendar : Calendar

        calendar = Calendar.getInstance()
        hourofday = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)

        var timepickerdialog = TimePickerDialog(this,2,
            OnTimeSetListener { timepicker, hourofday,minute->
                Toast.makeText(this, hourofday.toString()+"hour "+minute.toString()+"minute", Toast.LENGTH_SHORT).show();
            }, hourofday, minute, true)
            .show()


        var memberspinner = findViewById<Spinner>(R.id.member_spinner)
        var test_member = listOf<String>("JY","jy")

        val memberadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , test_member)
        memberadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberspinner.setAdapter(memberadapter)


        memberspinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                memberstring=   adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }




    fun CreateNewItem(view: View) {
        var description = findViewById<EditText>(R.id.description).text.toString()
        var member = findViewById<Spinner>(R.id.member_spinner)
        var class_level_1 = findViewById<EditText>(R.id.description).text.toString()
        var class_level_2 = findViewById<EditText>(R.id.description).text.toString()
        //自动填充时间
        AppDatabase.instance.userDAO().insertAll(
            Record(0,description, Date(System.currentTimeMillis()),Time(System.currentTimeMillis()),
            memberstring,class_level_1,class_level_2)
        )

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

