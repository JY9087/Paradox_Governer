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

    var categorystring = DEFAULT_CATEGORY_LIST.get(0)
    lateinit var subcategorystring : String
    var typestring = DEFAULT_TYPE_LIST[0]
    lateinit var subtypestring : String
    var account = "Account"
    var income = true


    var stringArray = arrayOf<String>("","","","","","","","","","")

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

        //Member
        var memberList = DAO.getAllMember()
        var memberStringList = mutableListOf<String>()
        for (members in memberList) {
            memberStringList.add(members.member)
        }
        InitSpinner(memberStringList.toList(),R.id.member_spinner, MEMBER_INDEX)

        //Category & Subcategory
        var categoryList = DAO.getAllCategory()
        var categoryStringList = mutableListOf<String>()
        for (categorys in categoryList) {
            categoryStringList.add(categorys.category)
        }
        InitCategotySpinner(categoryStringList.toList(),R.id.category_spinner, CATEGORY_INDEX)

        InitTypeSpinner(DEFAULT_TYPE_LIST,R.id.type_spinner, TYPE_INDEX)

        //Merchant
        var merchantList = DAO.getAllMerchant()
        var merchantStringList = mutableListOf<String>()
        for (merchants in merchantList) {
            merchantStringList.add(merchants.merchant)
        }
        InitSpinner(merchantStringList.toList(),R.id.merchant_spinner, MERCHANT_INDEX)

        //Item
        var itemList = DAO.getAllItem()
        var itemStringList = mutableListOf<String>()
        for (items in itemList) {
            itemStringList.add(items.item)
        }
        InitSpinner(itemStringList.toList(),R.id.item_spinner, ITEM_INDEX)


    }
    //End of OnCreate

    fun CreateNewItem(view: View) {

        //已完成
        var description = findViewById<EditText>(R.id.description).text.toString()

        //account使用什么值？



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

        /*
        AppDatabase.instance.userDAO().insertAll(
            Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
            memberstring,categorystring,subcategorystring,account,amount,typestring,income,"merchant","item")
        )
        */
        AppDatabase.instance.userDAO().insertAll(
            Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
                stringArray[MEMBER_INDEX] ,stringArray[CATEGORY_INDEX],stringArray[SUBCATEGORY_INDEX],account,amount,stringArray[TYPE_INDEX],income,
                stringArray[MERCHANT_INDEX],stringArray[ITEM_INDEX])
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





    fun NewMember(view : View) {
        var memberText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(memberText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(memberText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllMember(Member(0,memberText.text.toString()))
                    MemberAdapt()
                }


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

    fun InitSpinner(itemlist : List<String> , ID : Int , Index : Int)
    {
        var selectedSpinner = findViewById<Spinner>(ID)

        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemlist)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedSpinner.setAdapter(selectedSpinnerAdapter)

        selectedSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                stringArray[Index] = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }


    fun InitCategotySpinner(itemlist : List<String> , ID : Int , Index : Int)
    {
        var selectedSpinner = findViewById<Spinner>(ID)

        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemlist)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedSpinner.setAdapter(selectedSpinnerAdapter)

        selectedSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                stringArray[Index] = adapterView.getItemAtPosition(i) as String
                SubcategoryAdapt(stringArray[Index] ,R.id.subcategory_spinner , SUBCATEGORY_INDEX)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun SubcategoryAdapt(category : String, ID: Int , Index: Int) {
        var subcategoryList = DAO.getAllSubcategory(category)
        var subcategoryStringList = mutableListOf<String>()
        for (subcategorys in subcategoryList) {
            subcategoryStringList.add(subcategorys.subcategory)
        }
        var subcategoryspinner = findViewById<Spinner>(ID)

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
                stringArray[Index] = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }


    fun InitTypeSpinner(itemlist : List<String> , ID : Int , Index : Int)
    {
        var selectedSpinner = findViewById<Spinner>(ID)

        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemlist)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedSpinner.setAdapter(selectedSpinnerAdapter)

        selectedSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                stringArray[Index] = adapterView.getItemAtPosition(i) as String
                SubtypeAdapt(stringArray[Index])
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


    fun MemberAdapt()
    {
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
    }

}

