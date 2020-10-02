package com.example.paradoxgoverner



import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.sql.Date
import java.sql.Time
import java.text.DateFormat
import java.util.*


class CreateNewItem : AppCompatActivity() {
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
    }

    var rDate = DateFormat.getDateInstance()


    fun CreateNewItem(view: View) {
        val editText = findViewById<EditText>(R.id.Description)
        val message = editText.text.toString()
        var c = Calendar.getInstance()

        //自动填充时间
        AppDatabase.instance.userDAO().insertAll(Record(0,message, Date(System.currentTimeMillis())))

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

