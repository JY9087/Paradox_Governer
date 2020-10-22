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
    lateinit var rec : Record

    var stringArray = arrayOf<String>("","","","","","","","","","")

    var uid = 0
    var mcalendar = Calendar.getInstance()


    var memberStringList = mutableListOf<String>()
    var categoryStringList = mutableListOf<String>()
    var subcategoryStringList = mutableListOf<String>()
    var merchantStringList = mutableListOf<String>()
    var itemStringList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization_of_new_item)
        val DAO = AppDatabase.instance.userDAO()
        //查看是否有携带uid，如果有就修改uid变量的值
        uid = intent.getIntExtra(RECORD_UID,0)
        if(uid != 0){
            rec = AppDatabase.instance.userDAO().findByUid(uid)
        }

        //自动填充当前时间
        if(uid == 0){
            mcalendar.timeInMillis = System.currentTimeMillis()
            time_text.text = "时间：" + Date(System.currentTimeMillis()).toString() + " " + Time(System.currentTimeMillis()).toString()
        }
        else{
            mcalendar.timeInMillis = rec.time.time
            time_text.text = "时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString()
            //time_text.text = rec.date.toString() + " " + rec.time.toString()
        }


        //Member
        var memberList = DAO.getAllMember()
        for (members in memberList) {
            memberStringList.add(members.member)
        }
        InitSpinner(memberStringList.toList(),R.id.member_spinner, MEMBER_INDEX)

        //Category & Subcategory
        var categoryList = DAO.getAllCategory()
        for (categorys in categoryList) {
            categoryStringList.add(categorys.category)
        }
        InitCategotySpinner(categoryStringList.toList(),R.id.category_spinner, CATEGORY_INDEX)


        var subcategoryList = DAO.getAllSubcategory(categorystring)
        if(uid != 0){
            subcategoryList = DAO.getAllSubcategory(rec.category)
        }
        for (subcategorys in subcategoryList) {
            subcategoryStringList.add(subcategorys.subcategory)
        }

        //Type
        InitTypeSpinner(DEFAULT_TYPE_LIST,R.id.type_spinner, TYPE_INDEX)

        //Merchant
        var merchantList = DAO.getAllMerchant()
        for (merchants in merchantList) {
            merchantStringList.add(merchants.merchant)
        }
        InitSpinner(merchantStringList.toList(),R.id.merchant_spinner, MERCHANT_INDEX)

        //Item
        var itemList = DAO.getAllItem()
        for (items in itemList) {
            itemStringList.add(items.item)
        }
        InitSpinner(itemStringList.toList(),R.id.item_spinner, ITEM_INDEX)

        //也许要在Init之后修改？
        //新建
        if(uid == 0) {
            cancel_change_button.visibility = View.INVISIBLE
        }
        //修改
        else {
            confirm_button.text =getString(R.string.confirm_button_text_old)
            cancel_button.text =getString(R.string.cancel_button_text_old)
            money_amount?.setText(rec.amount.toString())
            description?.setText(rec.description)

            //假设不会有重名
            member_spinner?.setSelection(memberStringList.indexOf(rec.member))

            category_spinner?.setSelection(categoryStringList.indexOf(rec.category))

            subcategory_spinner?.setSelection(subcategoryStringList.indexOf(rec.subcategory))

            merchant_spinner?.setSelection(merchantStringList.indexOf(rec.merchant))

            item_spinner?.setSelection(itemStringList.indexOf(rec.item))

            type_spinner?.setSelection(DEFAULT_TYPE_LIST.indexOf(rec.type))

            stringArray[MEMBER_INDEX]=rec.member
            stringArray[CATEGORY_INDEX]=rec.category
            stringArray[SUBCATEGORY_INDEX]=rec.subcategory
            stringArray[MERCHANT_INDEX]=rec.merchant
            stringArray[ITEM_INDEX]=rec.item
            stringArray[TYPE_INDEX]=rec.type
            income = rec.income

            cancel_change_button.visibility = View.VISIBLE
        }
    }
    //End of OnCreate

    fun CreateNewRecord(view: View) {

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

        AppDatabase.instance.userDAO().insertAll(
            Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
                stringArray[MEMBER_INDEX] ,stringArray[CATEGORY_INDEX],stringArray[SUBCATEGORY_INDEX],account,amount,stringArray[TYPE_INDEX],income,
                stringArray[MERCHANT_INDEX],stringArray[ITEM_INDEX])
        )
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun DeleteRecord(view : View){
        if(uid != 0)
        {
            AppDatabase.instance.userDAO().delete(
                AppDatabase.instance.userDAO().findByUid(uid)
            )
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun CancelChange(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //新建  似乎只能写5个函数
    fun NewMember(view : View) {
        var memberText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入成员")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(memberText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(memberText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllMember(Member(0,memberText.text.toString()))
                    MemberAdapt()
                    member_spinner?.setSelection(memberStringList.indexOf(memberText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewCategory(view : View) {
        var categoryText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入一级分类")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(categoryText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(categoryText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllCategory(Category(0,categoryText.text.toString()))
                    CategoryAdapt()
                    category_spinner?.setSelection(categoryStringList.indexOf(categoryText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewSubcategory(view : View) {
        var subcategoryText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入二级分类")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(subcategoryText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(subcategoryText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllSubcategory(Subcategory(0,categorystring,subcategoryText.text.toString()))
                    SubcategoryAdapt(categorystring)
                    val DAO = AppDatabase.instance.userDAO()
                    var subcategoryList = DAO.getAllSubcategory(categorystring)
                    var subcategoryStringList = mutableListOf<String>()
                    for (subcategorys in subcategoryList) {
                        subcategoryStringList.add(subcategorys.subcategory)
                    }
                    subcategory_spinner?.setSelection(subcategoryStringList.indexOf(subcategoryText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewMerchant(view : View) {
        var merchantText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入商家")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(merchantText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(merchantText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllMerchant(Merchant(0,merchantText.text.toString()))
                    MerchantAdapt()
                    merchant_spinner?.setSelection(merchantStringList.indexOf(merchantText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewItem(view : View) {
        var itemText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入项目")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllItem(Item(0,itemText.text.toString()))
                    ItemAdapt()
                    item_spinner?.setSelection(itemStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }
    //End Of Customization


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
                time_text.text = "时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString()
            }, mhourofday, mminute, true)
            .show()

        var datepickerdialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener(){ datepicker, year,month,day->
                myear = year
                mmonth = month
                mday = day
                mcalendar.set(myear,mmonth,mday,mhourofday,mminute)
                time_text.text = "时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString()
            }, myear,mmonth,mday)
            .show()

        time_text.text = "时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString()
    }

    fun InitSpinner(itemlist : List<String> , ID : Int , Index : Int) {
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


    fun InitCategotySpinner(itemlist : List<String> , ID : Int , Index : Int) {
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
                categorystring = adapterView.getItemAtPosition(i) as String
                SubcategorySpinnerAdapt(stringArray[Index] ,R.id.subcategory_spinner , SUBCATEGORY_INDEX)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun SubcategorySpinnerAdapt(category : String, ID: Int, Index: Int) {
        val DAO = AppDatabase.instance.userDAO()
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


    fun InitTypeSpinner(itemlist : List<String> , ID : Int , Index : Int) {
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
                SubtypeSpinnerAdapt(stringArray[Index])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun SubtypeSpinnerAdapt(type : String) {
        var subtypeStringList = listOf<String>()
        when(type){
            "收入"->subtypeStringList = listOf<String>("收入")
            "支出"->subtypeStringList = listOf<String>("支出")
            //使用离谱的方法，终于解决Subtype了
            "借贷"->{
                if(uid != 0 && rec.income == false && rec.type != "支出"){
                    subtypeStringList = listOf<String>("支出","收入")
                }
                else{
                    subtypeStringList = listOf<String>("收入","支出")
                }
            }
            "转账"->{
                if(uid != 0 && rec.income == false && rec.type != "支出"){
                    subtypeStringList = listOf<String>("支出","收入")
                }
                else{
                    subtypeStringList = listOf<String>("收入","支出")
                }
            }
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

    fun MemberAdapt() {
        val DAO = AppDatabase.instance.userDAO()
        var memberList = DAO.getAllMember()
        memberStringList = mutableListOf<String>()
        for (members in memberList) {
            memberStringList.add(members.member)
        }
        var memberspinner = findViewById<Spinner>(R.id.member_spinner)
        val memberadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , memberStringList.toList())
        memberadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberspinner.setAdapter(memberadapter)
    }

    fun CategoryAdapt() {
        val DAO = AppDatabase.instance.userDAO()
        var categoryList = DAO.getAllCategory()
        categoryStringList = mutableListOf<String>()
        for (categorys in categoryList) {
            categoryStringList.add(categorys.category)
        }
        var categoryspinner = findViewById<Spinner>(R.id.category_spinner)
        val categoryadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , categoryStringList.toList())
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(categoryadapter)

    }

    fun SubcategoryAdapt(category: String) {
        val DAO = AppDatabase.instance.userDAO()
        var subcategoryList = DAO.getAllSubcategory(category)
        subcategoryStringList = mutableListOf<String>()
        for (subcategorys in subcategoryList) {
            subcategoryStringList.add(subcategorys.subcategory)
        }
        var subcategoryspinner = findViewById<Spinner>(R.id.subcategory_spinner)
        val subcategoryadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subcategoryStringList.toList())
        subcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcategoryspinner.setAdapter(subcategoryadapter)

    }

    fun MerchantAdapt() {
        val DAO = AppDatabase.instance.userDAO()
        var merchantList = DAO.getAllMerchant()
        merchantStringList = mutableListOf<String>()
        for (merchants in merchantList) {
            merchantStringList.add(merchants.merchant)
        }
        var merchantspinner = findViewById<Spinner>(R.id.merchant_spinner)
        val merchantadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , merchantStringList.toList())
        merchantadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        merchantspinner.setAdapter(merchantadapter)

    }

    fun ItemAdapt() {
        val DAO = AppDatabase.instance.userDAO()
        var itemList = DAO.getAllItem()
        itemStringList = mutableListOf<String>()
        for (items in itemList) {
            itemStringList.add(items.item)
        }
        var itemspinner = findViewById<Spinner>(R.id.item_spinner)
        val itemadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemStringList.toList())
        itemadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemspinner.setAdapter(itemadapter)

    }

}

