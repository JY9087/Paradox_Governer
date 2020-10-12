package com.example.paradoxgoverner


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
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
import kotlin.properties.Delegates


class CreateNewItem : AppCompatActivity() {

    lateinit var memberstring : String
    var categorystring = DEFAULT_CATEGORY_LIST.get(0)
    lateinit var subcategorystring : String

    var uid = 0
    var mcalendar = Calendar.getInstance()
    val DAO : UserDAO = AppDatabase.instance.userDAO()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization_of_new_item)


        var myear = mcalendar.get(Calendar.YEAR)
        var mmonth = mcalendar.get(Calendar.MONTH)
        var mday = mcalendar.get(Calendar.DAY_OF_MONTH)
        var mhourofday = mcalendar.get(Calendar.HOUR_OF_DAY)
        var mminute = mcalendar.get(Calendar.MINUTE)

        //在Click之前就设置了
        var timepickerdialog = TimePickerDialog(this,2,
            OnTimeSetListener { timepicker, hourofday,minute->
                mhourofday = hourofday
                mminute = minute
                mcalendar.set(myear,mmonth,mday,mhourofday,mminute)
            }, mhourofday, mminute, true)
            .show()

        var datepickerdialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener(){ datepicker, year,month,day->
                myear = year
                mmonth = month
                mday = day
                mcalendar.set(myear,mmonth,mday,mhourofday,mminute)
            }, myear,mmonth,mday)
            .show()

        //查看是否有携带uid，如果有就修改uid变量的值
        uid = intent.getIntExtra(RECORD_UID,0)

        //新建
        if(uid == 0) {

        }

        //修改
        else {
            confirm_button.text =getString(R.string.confirm_button_text_old)
            cancel_button.text =getString(R.string.cancel_button_text_old)
            money_amount?.setHint(AppDatabase.instance.userDAO().findByUid(uid).amount.toString())

        }


        var memberList = DAO.getAllMember()
        var memberStringList = mutableListOf<String>()
        for (members in memberList) {
            memberStringList.add(members.member)
        }


        var memberspinner = findViewById<Spinner>(R.id.member_spinner)

        val memberadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , memberStringList.toList())
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
                memberstring = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })


        //Category
        var categoryList = DAO.getAllCategory()
        var categoryStringList = mutableListOf<String>()
        for (categorys in categoryList) {
            categoryStringList.add(categorys.category)
        }

        var categoryspinner = findViewById<Spinner>(R.id.category_spinner)
        var categorystring = DEFAULT_CATEGORY_LIST.get(0)
        val categoryadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , categoryStringList.toList())
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(categoryadapter)

        categoryspinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                categorystring = adapterView.getItemAtPosition(i) as String
                SubcategoryAdapt(categorystring)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })


    }


    fun CreateNewItem(view: View) {

        //已完成
        var description = findViewById<EditText>(R.id.description).text.toString()

        //未完成，只有变量名。变量值用来凑数

        var class_level_2 = findViewById<EditText>(R.id.description).text.toString()
        var account = findViewById<EditText>(R.id.description).text.toString()
        var category = findViewById<EditText>(R.id.description).text.toString()

        //这一点也不优雅
        //一定要改掉它
        //三种情况：新建时未赋值  修改时未赋值   已赋值

        var amount : Float

        //默认值，同时赋予初始值。若新建时未赋值则使用初始值
        amount = 0F

        //已修改
        if( !findViewById<EditText>(R.id.money_amount).text.toString().isEmpty()){
            amount = findViewById<EditText>(R.id.money_amount).text.toString().toFloat()
        }
        //修改时未赋值
        else if(uid != 0) {
            amount = AppDatabase.instance.userDAO().findByUid(uid).amount
        }


        //自动填充时间
        AppDatabase.instance.userDAO().insertAll(
            Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
            memberstring,categorystring,class_level_2,account,amount,category)
        )

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun DeleteItem(view : View){
        if(uid != 0)
        {
            AppDatabase.instance.userDAO().delete(
                AppDatabase.instance.userDAO().findByUid(uid)
            )
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun SubcategoryAdapt(category : String) {
        var subcategoryList = DAO.getAllSubcategory(category)
        var subcategoryStringList = mutableListOf<String>()
        for (subcategorys in subcategoryList) {
            subcategoryStringList.add(subcategorys.subcategory)
        }
        var subcategoryspinner = findViewById<Spinner>(R.id.subcategory_spinner)

        val subcategoryadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subcategoryStringList.toList())
        subcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcategoryspinner.setAdapter(subcategoryadapter)

        subcategoryspinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                subcategorystring = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun NewMember(view : View)
    {
        //BUG添加空白字符串
        var memberText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(memberText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(memberText.text.toString() != "")
                    AppDatabase.instance.userDAO().insertAllMember(Member(0,memberText.text.toString()))
            })
            .setNegativeButton("取消", null)
            .show()
    }

}

