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


class CreateNewItem : AppCompatActivity() {

    lateinit var memberstring : String
    var categorystring = DEFAULT_CATEGORY_LIST.get(0)
    lateinit var subcategorystring : String
    lateinit var typestring : String
    lateinit var subtypestring : String
    var income = true
    lateinit var merchantstring : String
    lateinit var itemstring : String

    var uid = 0
    var mcalendar = Calendar.getInstance()
    val DAO : UserDAO = AppDatabase.instance.userDAO()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization_of_new_item)

        //自动填充当前时间
        var myear = mcalendar.get(Calendar.YEAR)
        var mmonth = mcalendar.get(Calendar.MONTH)
        var mday = mcalendar.get(Calendar.DAY_OF_MONTH)
        var mhourofday = mcalendar.get(Calendar.HOUR_OF_DAY)
        var mminute = mcalendar.get(Calendar.MINUTE)
        mcalendar.set(myear,mmonth,mday,mhourofday,mminute)

        time_text.text = "时间： "+myear.toString()+"年"+mmonth.toString()+"月"+mday.toString()+"日 "+mhourofday.toString()+"时"+mminute.toString()+"分"

        //查看是否有携带uid，如果有就修改uid变量的值
        uid = intent.getIntExtra(RECORD_UID,0)

        //新建
        if(uid == 0) {
            cancel_change_button.visibility = View.INVISIBLE
        }

        //修改
        else {
            confirm_button.text =getString(R.string.confirm_button_text_old)
            cancel_button.text =getString(R.string.cancel_button_text_old)
            money_amount?.setHint(AppDatabase.instance.userDAO().findByUid(uid).amount.toString())
            cancel_change_button.visibility = View.VISIBLE
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



        var typespinner = findViewById<Spinner>(R.id.type_spinner)
        val typeadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , DEFAULT_TYPE_LIST)
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typespinner.setAdapter(typeadapter)

        typespinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                typestring = adapterView.getItemAtPosition(i) as String
                SubtypeAdapt(typestring)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })





        var merchantList = DAO.getAllMerchant()
        var merchantStringList = mutableListOf<String>()
        for (merchants in merchantList) {
            merchantStringList.add(merchants.merchant)
        }

        var merchantspinner = findViewById<Spinner>(R.id.merchant_spinner)

        val merchantadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , merchantStringList.toList())
        merchantadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        merchantspinner.setAdapter(merchantadapter)

        merchantspinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                merchantstring = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })



        var itemList = DAO.getAllItem()
        var itemStringList = mutableListOf<String>()
        for (items in itemList) {
            itemStringList.add(items.item)
        }

        var itemspinner = findViewById<Spinner>(R.id.item_spinner)

        val itemadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemStringList.toList())
        itemadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemspinner.setAdapter(itemadapter)

        itemspinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                itemstring = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })


    }
    //End of OnCreate

    fun CreateNewItem(view: View) {

        //已完成
        var description = findViewById<EditText>(R.id.description).text.toString()

        //未完成，只有变量名。变量值用来凑数

        var class_level_2 = findViewById<EditText>(R.id.description).text.toString()
        var account = findViewById<EditText>(R.id.description).text.toString()


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

        AppDatabase.instance.userDAO().insertAll(
            Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
            memberstring,categorystring,subcategorystring,account,amount,typestring,income,"merchant","item")
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

    fun CancelChange(view: View)
    {
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


    fun SubtypeAdapt(type : String) {
        var subtypeStringList = listOf<String>()
        when(type){
            "收入"->subtypeStringList = listOf<String>("收入")
            "支出"->subtypeStringList = listOf<String>("支出")
            "借贷"->subtypeStringList = listOf<String>("收入","支出")
            "转账"->subtypeStringList = listOf<String>("收入","支出")
        }
        var subtypespinner = findViewById<Spinner>(R.id.sub_type_spinner)

        val subtypeadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subtypeStringList)
        subtypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subtypespinner.setAdapter(subtypeadapter)

        subtypespinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                subtypestring = adapterView.getItemAtPosition(i) as String
                if(subtypestring == "收入")
                    income = true
                else if(subtypestring == "支出")
                    income = false
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun NewMember(view : View) {
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

    fun SelectTime(view : View){
        var myear = mcalendar.get(Calendar.YEAR)
        var mmonth = mcalendar.get(Calendar.MONTH)
        var mday = mcalendar.get(Calendar.DAY_OF_MONTH)
        var mhourofday = mcalendar.get(Calendar.HOUR_OF_DAY)
        var mminute = mcalendar.get(Calendar.MINUTE)

        var timepickerdialog = TimePickerDialog(this,2,
            OnTimeSetListener { timepicker, hourofday,minute->
                mhourofday = hourofday
                mminute = minute
                mcalendar.set(myear,mmonth,mday,mhourofday,mminute)
                time_text.text = "时间： "+myear.toString()+"年"+mmonth.toString()+"月"+mday.toString()+"日 "+mhourofday.toString()+"时"+mminute.toString()+"分"
            }, mhourofday, mminute, true)
            .show()

        var datepickerdialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener(){ datepicker, year,month,day->
                myear = year
                mmonth = month
                mday = day
                mcalendar.set(myear,mmonth,mday,mhourofday,mminute)
                time_text.text = "时间： "+myear.toString()+"年"+mmonth.toString()+"月"+mday.toString()+"日 "+mhourofday.toString()+"时"+mminute.toString()+"分"
            }, myear,mmonth,mday)
            .show()

        time_text.text = "时间： "+myear.toString()+"年"+mmonth.toString()+"月"+mday.toString()+"日 "+mhourofday.toString()+"时"+mminute.toString()+"分"
    }

}

