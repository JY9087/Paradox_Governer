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
    var accountStringList = mutableListOf<String>()
    var subtypeStringList = listOf<String>()
    var TemplateName = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_customization_of_new_item)

        val DAO = AppDatabase.instance.userDAO()

        //查看是否有携带uid，如果有就修改uid变量的值
        uid = intent.getIntExtra(RECORD_UID,0)
        if(uid != 0){
            rec = AppDatabase.instance.userDAO().findRecordByUid(uid)
        }

        //自动填充当前时间
        if(uid == 0){
            mcalendar.timeInMillis = System.currentTimeMillis() }
        else{
            mcalendar.timeInMillis = rec.time.time }
        //Todo : 使用PlaceHolder
        time_text.text = ("时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString())


        //Member
        memberStringList.add(VOID_ITEM)
        for (members in DAO.getAllMember()) {
            if(members.member != VOID_ITEM){
                memberStringList.add(members.member)
            }
        }
        InitSpinner(memberStringList.toList(),R.id.member_spinner, MEMBER_INDEX)



        //Category & Subcategory
        for (categorys in DAO.getAllCategory()) {
            if(categorys.category != VOID_ITEM){
                categoryStringList.add(categorys.category)
            }
        }
        categoryStringList.add(VOID_ITEM)
        initCategotySpinner(categoryStringList.toList())
        if(uid != 0){
            category_spinner?.setSelection(categoryStringList.indexOf(rec.category))
        }


        var subcategoryList = DAO.getAllSubcategory(categorystring)
        if(uid != 0){
            subcategoryList = DAO.getAllSubcategory(rec.category)
        }
        for (subcategorys in subcategoryList) {
            if(subcategorys.subcategory != VOID_ITEM){
                subcategoryStringList.add(subcategorys.subcategory)
            }
        }
        subcategoryStringList.add(VOID_ITEM)
        subcategorySpinnerAdapt(categorystring)


        //Type
        initTypeSpinner(DEFAULT_TYPE_LIST)

        //Merchant
        merchantStringList.add(VOID_ITEM)
        for (merchants in DAO.getAllMerchant()) {
            if(merchants.merchant != VOID_ITEM){
                merchantStringList.add(merchants.merchant)
            }
        }

        InitSpinner(merchantStringList.toList(),R.id.merchant_spinner, MERCHANT_INDEX)

        //Item
        itemStringList.add(VOID_ITEM)
        for (items in DAO.getAllItem()) {
            if(items.item != VOID_ITEM){
                itemStringList.add(items.item)
            }
        }

        InitSpinner(itemStringList.toList(),R.id.item_spinner, ITEM_INDEX)

        //Account
        for (accounts in DAO.getAllAccount()) {
            if(accounts.account != ALL_ACCOUNT && accounts.account != VOID_ITEM){
                accountStringList.add(accounts.account)
            }
        }
        accountStringList.add(VOID_ITEM)
        InitSpinner(accountStringList.toList(),R.id.account_spinner, ACCOUNT_INDEX)

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

            //rec.subcategory是对的，但就硬不能setSelection
            //其他都可以setSelection
            subcategory_spinner?.setSelection(subcategoryStringList.indexOf(rec.subcategory))

            merchant_spinner?.setSelection(merchantStringList.indexOf(rec.merchant))

            item_spinner?.setSelection(itemStringList.indexOf(rec.item))

            type_spinner?.setSelection(DEFAULT_TYPE_LIST.indexOf(rec.type))

            account_spinner?.setSelection(accountStringList.indexOf(rec.account))

            stringArray[MEMBER_INDEX]=rec.member
            stringArray[CATEGORY_INDEX]=rec.category
            stringArray[SUBCATEGORY_INDEX]=rec.subcategory
            stringArray[MERCHANT_INDEX]=rec.merchant
            stringArray[ITEM_INDEX]=rec.item
            stringArray[TYPE_INDEX]=rec.type
            stringArray[ACCOUNT_INDEX]=rec.account
            income = rec.income

            cancel_change_button.visibility = View.VISIBLE
        }

        InitTemplateSpinner()
    }
    //End of OnCreate



    
    fun createNewRecord(view: View) {

        var description = findViewById<EditText>(R.id.description).text.toString()

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
            amount = AppDatabase.instance.userDAO().findRecordByUid(uid).amount
        }

        if(amount > 10000000000){
            Toast.makeText(this,"金额过大，超过10,000,000,000，请重新输入",Toast.LENGTH_SHORT).show()
        }
        else{

            if(DashboardActivity.sourceDashFlag){
                statisticsActivity.recordList2.remove(AppDatabase.instance.userDAO().findRecordByUid(uid))
                statisticsActivity.recordList2.add(
                    Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
                        stringArray[MEMBER_INDEX] ,stringArray[CATEGORY_INDEX],stringArray[SUBCATEGORY_INDEX],
                        stringArray[ACCOUNT_INDEX],amount,stringArray[TYPE_INDEX],income,
                        stringArray[MERCHANT_INDEX],stringArray[ITEM_INDEX])
                )
            }

            AppDatabase.instance.userDAO().insertAll(
                Record(uid,description, Date(mcalendar.timeInMillis),Time(mcalendar.timeInMillis),
                    stringArray[MEMBER_INDEX] ,stringArray[CATEGORY_INDEX],stringArray[SUBCATEGORY_INDEX],
                    stringArray[ACCOUNT_INDEX],amount,stringArray[TYPE_INDEX],income,
                    stringArray[MERCHANT_INDEX],stringArray[ITEM_INDEX])
            )

            if(DashboardActivity.sourceDashFlag){
                DashboardActivity.sourceDashFlag = false
                statisticsActivity.searchFlag = true
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    
    fun deleteRecord(view : View){
        if(uid != 0)
        {

            if(DashboardActivity.sourceDashFlag){
                statisticsActivity.recordList2.remove(AppDatabase.instance.userDAO().findRecordByUid(uid))
            }

            AppDatabase.instance.userDAO().delete(
                AppDatabase.instance.userDAO().findRecordByUid(uid)
            )
        }

        if(DashboardActivity.sourceDashFlag){
            DashboardActivity.sourceDashFlag = false
            statisticsActivity.searchFlag = true
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    
    fun cancelChange(view: View) {

        if(DashboardActivity.sourceDashFlag){
            DashboardActivity.sourceDashFlag = false
            statisticsActivity.searchFlag = true
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    //新建  似乎只能写5个函数
    
    fun NewMember(view : View) {
        val memberText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入成员")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(memberText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(memberText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllMember(Member(0,memberText.text.toString()))
                    memberAdapt()
                    member_spinner?.setSelection(memberStringList.indexOf(memberText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewCategory(view : View) {
        val categoryText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入一级分类")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(categoryText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(categoryText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllCategory(Category(0,categoryText.text.toString()))
                    AppDatabase.instance.userDAO().insertAllSubcategory(Subcategory(0,categoryText.text.toString(),
                        VOID_ITEM))
                    categoryAdapt()
                    category_spinner?.setSelection(categoryStringList.indexOf(categoryText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewSubcategory(view : View) {
        val subcategoryText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("请输入二级分类")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(subcategoryText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(subcategoryText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllSubcategory(Subcategory(0,categorystring,subcategoryText.text.toString()))
                    SubcategoryAdapt(categorystring)
                    val DAO = AppDatabase.instance.userDAO()
                    val subcategoryList = DAO.getAllSubcategory(categorystring)
                    val subcategoryStringList = mutableListOf<String>()
                    for (subcategory in subcategoryList) {
                        subcategoryStringList.add(subcategory.subcategory)
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
                    merchantAdapt()
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
                    itemAdapt()
                    item_spinner?.setSelection(itemStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewAccount(view : View) {
        var itemText = EditText(this)
        android.app.AlertDialog.Builder(this)
            .setTitle("请输入账户")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    AppDatabase.instance.userDAO().insertAllAccount(Account(0,itemText.text.toString()))
                    accountAdapt()
                    account_spinner?.setSelection(accountStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }
    //End Of Customization


    //选择时间
    
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
                time_text.text = ("时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString())
            }, mhourofday, mminute, true)
            .show()

        var datepickerdialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener(){ datepicker, year,month,day->
                myear = year
                mmonth = month
                mday = day
                mcalendar.set(myear,mmonth,mday,mhourofday,mminute)
                time_text.text = ("时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString())
            }, myear,mmonth,mday)
            .show()

        time_text.text = ("时间：" + Date(mcalendar.timeInMillis).toString() + " " + Time(mcalendar.timeInMillis).toString())
    }


    //初始化通用Spinner(Member,Merchant,Item)
    
    fun InitSpinner(itemlist : List<String> , ID : Int , Index : Int) {
        val selectedSpinner = findViewById<Spinner>(ID)

        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemlist)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        selectedSpinner.adapter = selectedSpinnerAdapter

        selectedSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                //用于新建/修改Record
                stringArray[Index] = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }


    }

    //初始化一级目录Spinner，同时调用二级目录Spinner初始化

    fun initCategotySpinner(itemList : List<String>) {
        val selectedSpinner = findViewById<Spinner>(R.id.category_spinner)

        if(uid != 0){
            categorystring = rec.category
        }

        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemList)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        selectedSpinner.adapter = selectedSpinnerAdapter

        selectedSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                stringArray[CATEGORY_INDEX] = adapterView.getItemAtPosition(i) as String
                categorystring = adapterView.getItemAtPosition(i) as String
                SubcategoryAdapt(categorystring)
                if(uid != 0){
                    subcategory_spinner?.setSelection(subcategoryStringList.indexOf(rec.subcategory))
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun subcategorySpinnerAdapt(category : String) {
        val DAO = AppDatabase.instance.userDAO()
        val subcategoryList = DAO.getAllSubcategory(category)
        val subcategoryStringList = mutableListOf<String>()
        for (subcategory in subcategoryList) {
            subcategoryStringList.add(subcategory.subcategory)
        }
        val subcategoryspinner = findViewById<Spinner>(R.id.subcategory_spinner)

        val subcategoryAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subcategoryStringList.toList())
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subcategoryspinner.adapter = subcategoryAdapter

        subcategoryspinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                stringArray[SUBCATEGORY_INDEX] = adapterView.getItemAtPosition(i) as String
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        }
    }

    //初始化类型Spinner
    
    fun initTypeSpinner(itemList : List<String>) {
        val selectedSpinner = findViewById<Spinner>(R.id.type_spinner)

        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemList)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedSpinner.adapter = selectedSpinnerAdapter

        selectedSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                stringArray[TYPE_INDEX] = adapterView.getItemAtPosition(i) as String
                subtypeSpinnerAdapt(stringArray[TYPE_INDEX])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        }
    }


    //初始化二级分类
    
    fun subtypeSpinnerAdapt(type : String) {
        when(type){
            "收入"->subtypeStringList = listOf<String>("收入")
            "支出"->subtypeStringList = listOf<String>("支出")
            //使用离谱的方法，终于解决Subtype了
            "借贷"->{
                subtypeStringList = if(uid != 0 && !rec.income && rec.type != "支出"){
                    listOf<String>("支出","收入")
                } else{
                    listOf<String>("收入","支出")
                }
            }
            "转账"->{
                subtypeStringList = if(uid != 0 && !rec.income && rec.type != "支出"){
                    listOf<String>("支出","收入")
                } else{
                    listOf<String>("收入","支出")
                }
            }
        }
        val subTypeSpinner = findViewById<Spinner>(R.id.sub_type_spinner)

        val subTypeAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subtypeStringList)
        subTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subTypeSpinner.adapter = subTypeAdapter

        subTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
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
        }
    }

    //成员Spinner Adapt
    
    fun memberAdapt() {
        memberStringList.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (members in DAO.getAllMember()) {
            if(members.member != VOID_ITEM){
                memberStringList.add(members.member)
            }
        }
        memberStringList.add(VOID_ITEM)
        val memberSpinner = findViewById<Spinner>(R.id.member_spinner)
        val memberAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , memberStringList.toList())
        memberAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        memberSpinner.adapter = memberAdapter
    }

    fun categoryAdapt() {
        categoryStringList.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (category in DAO.getAllCategory()) {
            if(category.category != VOID_ITEM){
                categoryStringList.add(category.category)
            }
        }
        categoryStringList.add(VOID_ITEM)
        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)
        val categoryAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , categoryStringList.toList())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categorySpinner.adapter = categoryAdapter

    }

    fun SubcategoryAdapt(category: String) {
        subcategoryStringList.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (subcategory in DAO.getAllSubcategory(category)) {
            if(subcategory.subcategory != VOID_ITEM){
                subcategoryStringList.add(subcategory.subcategory)
            }
        }
        subcategoryStringList.add(VOID_ITEM)

        val subcategorySpinner = findViewById<Spinner>(R.id.subcategory_spinner)
        val subcategoryAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subcategoryStringList.toList())
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subcategorySpinner.adapter = subcategoryAdapter

    }

    fun merchantAdapt() {
        merchantStringList.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (merchants in DAO.getAllMerchant()) {
            if(merchants.merchant != VOID_ITEM){
                merchantStringList.add(merchants.merchant)
            }
        }
        merchantStringList.add(VOID_ITEM)
        val merchantSpinner = findViewById<Spinner>(R.id.merchant_spinner)
        val merchantAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , merchantStringList.toList())
        merchantAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        merchantSpinner.adapter = merchantAdapter

    }

    fun itemAdapt() {
        itemStringList.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (items in DAO.getAllItem()) {
            if(items.item != VOID_ITEM){
                itemStringList.add(items.item)
            }
        }
        itemStringList.add(VOID_ITEM)
        val itemSpinner = findViewById<Spinner>(R.id.item_spinner)
        val itemAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemStringList.toList())
        itemAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        itemSpinner.adapter = itemAdapter
    }

    fun accountAdapt() {
        accountStringList.clear()
        val DAO = AppDatabase.instance.userDAO()
        for (accounts in DAO.getAllAccount()) {
            if(accounts.account != ALL_ACCOUNT && accounts.account != VOID_ITEM){
                accountStringList.add(accounts.account)
            }
        }
        accountStringList.add(VOID_ITEM)
        val itemSpinner = findViewById<Spinner>(R.id.account_spinner)
        val itemAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , accountStringList.toList())
        itemAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        itemSpinner.adapter = itemAdapter
    }


    fun SaveTemplate(view : View) {
        val itemText = EditText(this)
        android.app.AlertDialog.Builder(this)
            .setTitle("请输入模板名称")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    NewTemplate(itemText.text.toString())
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    //重名覆盖
    fun NewTemplate(name : String){
        TemplateName = name
        val DAO = AppDatabase.instance.userDAO()

        if(DAO.findTemplateByString(name).isEmpty()){
            DAO.insertAllTemplate(
                Template(0,name,stringArray[MEMBER_INDEX] ,stringArray[CATEGORY_INDEX],stringArray[SUBCATEGORY_INDEX],
                    stringArray[ACCOUNT_INDEX],stringArray[TYPE_INDEX],income,
                    stringArray[MERCHANT_INDEX],stringArray[ITEM_INDEX])
            )
        }
        else{
            DAO.insertAllTemplate(
                Template(DAO.findTemplateByString(name)[0].uid,name,stringArray[MEMBER_INDEX] ,
                    stringArray[CATEGORY_INDEX],stringArray[SUBCATEGORY_INDEX],
                    stringArray[ACCOUNT_INDEX],stringArray[TYPE_INDEX],income,
                    stringArray[MERCHANT_INDEX],stringArray[ITEM_INDEX]))
        }
        InitTemplateSpinner()
    }

    fun DeleteTemplate(view : View) {
        android.app.AlertDialog.Builder(this)
            .setTitle("确认删除模板：$TemplateName?")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                AppDatabase.instance.userDAO().deleteTemplate(AppDatabase.instance.userDAO().findTemplateByString(TemplateName)[0])
                InitTemplateSpinner()
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun InitTemplateSpinner() {
        val selectedSpinner = findViewById<Spinner>(R.id.TemplateSpinner)

        val DAO = AppDatabase.instance.userDAO()

        val TemplateNameList = mutableListOf<String>(VOID_TEMPLATE)
        for(temp in DAO.getAllTemplate()){
            TemplateNameList.add(temp.name)
        }

        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , TemplateNameList.toList())
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        selectedSpinner.adapter = selectedSpinnerAdapter

        selectedSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                //用于新建/修改Record
                TemplateName =adapterView.getItemAtPosition(i) as String
                if(TemplateName != VOID_TEMPLATE){
                    DeleteTemplateButton.visibility = View.VISIBLE
                    val temp = AppDatabase.instance.userDAO().findTemplateByString(TemplateName)[0]

                    //使用模板
                    member_spinner.setSelection(memberStringList.indexOf(temp.member))
                    category_spinner.setSelection(categoryStringList.indexOf(temp.category))
                    subcategory_spinner.setSelection(subcategoryStringList.indexOf(temp.subcategory))
                    merchant_spinner.setSelection(merchantStringList.indexOf(temp.merchant))
                    item_spinner.setSelection(itemStringList.indexOf(temp.item))
                    account_spinner.setSelection(accountStringList.indexOf(temp.account))
                    type_spinner.setSelection(DEFAULT_TYPE_LIST.indexOf(temp.type))
                    if(temp.income){
                        sub_type_spinner.setSelection(subtypeStringList.indexOf("收入"))
                    } else{
                        sub_type_spinner.setSelection(subtypeStringList.indexOf("收入"))
                    }
                } else{
                    DeleteTemplateButton.visibility = View.INVISIBLE
                }


            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }


    }



}

