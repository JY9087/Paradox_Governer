package com.example.paradoxgoverner

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_personal.*
import kotlin.properties.Delegates

class PersonalActivity : AppCompatActivity() {

    companion object {
        var instance: PersonalActivity by Delegates.notNull()
        fun instance() = instance
        var themeColor = R.style.CustomizedAppThemePurple
    }

    var stringList = mutableListOf<String>()
    var type = MEMBER_INDEX
    var uid = 0
    var subcategory_uid = 0
    var categoryString = DEFAULT_CATEGORY_LIST[0]
    var staticThemeColorString = ""
    var lastModified = mutableListOf<String>("","","","","","","","","","")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(themeColor)
        when(themeColor){
            R.style.CustomizedAppThemePurple -> staticThemeColorString = THEME_PURPLE
            R.style.CustomizedAppThemeRed -> staticThemeColorString = THEME_RED
            R.style.CustomizedAppThemePink -> staticThemeColorString = THEME_PINK
            R.style.CustomizedAppThemeOrange -> staticThemeColorString = THEME_ORANGE
            R.style.CustomizedAppThemeYellow -> staticThemeColorString = THEME_YELLOW
            R.style.CustomizedAppThemeBlue -> staticThemeColorString = THEME_BLUE
            R.style.CustomizedAppThemeGreen -> staticThemeColorString = THEME_GREEN
        }

        setContentView(R.layout.activity_personal)


        val bottomNavigatior = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigatior.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_dashboard-> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_graph -> {
                    val intent = Intent(this, GraphActivity::class.java)
                    startActivity(intent)
                    if (MainActivity.versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_personal -> {}
            }
            true
        })
        bottomNavigatior.selectedItemId = R.id.navigation_personal

        InitSpinner()
        InitThemeSpinner()
        theme_spinner.setSelection(THEME_LIST.indexOf(staticThemeColorString))
    }

    //让VOID_ITEM显示吧。但不应修改
    fun InitSpinner() {
        val selectedSpinner = findViewById<Spinner>(R.id.personal_custom_spinner)
        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , CUSTOMIZED_LIST)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedSpinner.adapter = selectedSpinnerAdapter
        val DAO = AppDatabase.instance.userDAO()
        selectedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                personal_custom_spinner3.visibility = View.INVISIBLE
                personal_custom_button3.visibility = View.INVISIBLE
                personal_custom_button4.visibility = View.INVISIBLE
                stringList.clear()
                //kotlin list下标从0开始
                when(adapterView.getItemAtPosition(i) as String){
                    CUSTOMIZED_LIST[0] -> {
                        type = ACCOUNT_INDEX
                        for (accounts in DAO.getAllAccount()) {
                            if(accounts.account != ALL_ACCOUNT && accounts.account != VOID_ITEM){
                                stringList.add(accounts.account)
                            }
                        }
                        stringList.add(VOID_ITEM)
                        InitSecondSpinner(stringList)
                    }
                    CUSTOMIZED_LIST[1] -> {
                        type = CATEGORY_INDEX
                        for (categorys in DAO.getAllCategory()) {
                            if(categorys.category != VOID_ITEM){
                                stringList.add(categorys.category)
                            }
                        }
                        stringList.add(VOID_ITEM)
                        personal_custom_spinner3.visibility = View.VISIBLE
                        personal_custom_button3.visibility = View.VISIBLE
                        personal_custom_button4.visibility = View.VISIBLE
                        InitSecondSpinner(stringList)
                    }
                    CUSTOMIZED_LIST[2] -> {
                        type = MERCHANT_INDEX
                        for (merchants in DAO.getAllMerchant()) {
                            if(merchants.merchant != VOID_ITEM){
                                stringList.add(merchants.merchant)
                            }
                        }
                        stringList.add(VOID_ITEM)
                        InitSecondSpinner(stringList)
                    }

                    CUSTOMIZED_LIST[3] -> {
                        type = ITEM_INDEX
                        for (items in DAO.getAllItem()) {
                            if(items.item != VOID_ITEM){
                                stringList.add(items.item)
                            }
                        }
                        stringList.add(VOID_ITEM)
                        InitSecondSpinner(stringList)
                    }
                    CUSTOMIZED_LIST[4] -> {
                        type = MEMBER_INDEX
                        for (members in DAO.getAllMember()) {
                            if(members.member != VOID_ITEM){
                                stringList.add(members.member)
                            }
                        }
                        stringList.add(VOID_ITEM)
                        InitSecondSpinner(stringList)
                    }

                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        }
    }

    
    fun InitSecondSpinner(itemlist : List<String>) {
        val selectedSpinner = findViewById<Spinner>(R.id.personal_custom_spinner2)

        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemlist)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        selectedSpinner.setAdapter(selectedSpinnerAdapter)
        val DAO = AppDatabase.instance.userDAO()
        selectedSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                // Get the spinner selected item text
                when(type){
                    MEMBER_INDEX -> {uid = DAO.findMemberByString(adapterView.getItemAtPosition(i).toString())[0].uid }
                    CATEGORY_INDEX -> { uid = DAO.findCategoryByString(adapterView.getItemAtPosition(i).toString())[0].uid
                        categoryString = adapterView.getItemAtPosition(i) as String
                        SubcategorySpinnerAdapt()
                    }
                    MERCHANT_INDEX -> {uid = DAO.findMerchantByString(adapterView.getItemAtPosition(i).toString())[0].uid }
                    ITEM_INDEX -> {uid = DAO.findItemByString(adapterView.getItemAtPosition(i).toString())[0].uid }
                    ACCOUNT_INDEX -> {uid = DAO.findAccountByString(adapterView.getItemAtPosition(i).toString())[0].uid }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
        if(lastModified[type] != "")
            personal_custom_spinner2?.setSelection(stringList.indexOf(lastModified[type]))
    }

    //对VOID_ITEM的处理
    fun SubcategorySpinnerAdapt() {
        val DAO = AppDatabase.instance.userDAO()
        val subcategoryList = DAO.getAllSubcategory(categoryString)
        val subcategoryStringList = mutableListOf<String>()
        for (subcategorys in subcategoryList) {
            subcategoryStringList.add(subcategorys.subcategory)
        }
        val subcategoryspinner = findViewById<Spinner>(R.id.personal_custom_spinner3)

        val subcategoryadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subcategoryStringList.toList())
        subcategoryadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        subcategoryspinner.adapter = subcategoryadapter

        subcategoryspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                subcategory_uid = DAO.findSubcategoryByString(categoryString,adapterView.getItemAtPosition(i).toString())[0].uid
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun NewCustomizedItem(view : View) {
        var title = "请输入"
        var typeLabel =""
        when(type){
            MEMBER_INDEX->typeLabel = CUSTOMIZED_LIST[0]
            CATEGORY_INDEX->typeLabel = CUSTOMIZED_LIST[1]
            MERCHANT_INDEX->typeLabel = CUSTOMIZED_LIST[2]
            ITEM_INDEX->typeLabel = CUSTOMIZED_LIST[3]
        }
        title += typeLabel
        val DAO = AppDatabase.instance.userDAO()
        val itemText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != "" && itemText.text.toString() != VOID_ITEM) {
                    val txt = itemText.text.toString()
                    when (type) {
                        MEMBER_INDEX -> DAO.insertAllMember(Member(0, txt))
                        //新增一级分类时，二级分类新增"无"
                        CATEGORY_INDEX -> {
                            DAO.insertAllCategory(Category(0, txt))
                            DAO.insertAllSubcategory(Subcategory(0,txt, VOID_ITEM))
                        }
                        MERCHANT_INDEX -> DAO.insertAllMerchant(Merchant(0, txt))
                        ITEM_INDEX -> DAO.insertAllItem(Item(0, txt))
                        ACCOUNT_INDEX ->  DAO.insertAllAccount(Account(0, txt))
                    }
                    lastModified[type] = txt
                    InitSpinner()
                    personal_custom_spinner?.setSelection(CUSTOMIZED_LIST.indexOf(typeLabel))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    fun NewCustomizedSubcategory(view : View) {
        val title = "请输入二级分类"
        val DAO = AppDatabase.instance.userDAO()
        val itemText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                //不能新建空Subcategory ; 不能在VOID_ITEM Category下新建Subcategory
                if(itemText.text.toString() != "" && categoryString != VOID_ITEM){
                    DAO.insertAllSubcategory(Subcategory(0,categoryString,itemText.text.toString()))
                    SubcategorySpinnerAdapt()
                    val subcategoryStringList = mutableListOf<String>()
                    for (subcategorys in DAO.getAllSubcategory(categoryString)) {
                        subcategoryStringList.add(subcategorys.subcategory)
                    }
                    personal_custom_spinner3?.setSelection(subcategoryStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

    
    fun ChangeCustomizedItem(view : View) {
        var title = "请输入"
        var typeLabel =""
        when(type){
            MEMBER_INDEX->typeLabel = CUSTOMIZED_LIST[4]
            CATEGORY_INDEX->typeLabel = CUSTOMIZED_LIST[1]
            MERCHANT_INDEX->typeLabel = CUSTOMIZED_LIST[2]
            ITEM_INDEX->typeLabel = CUSTOMIZED_LIST[3]
            ACCOUNT_INDEX->typeLabel = CUSTOMIZED_LIST[0]
        }
        title += typeLabel
        val DAO = AppDatabase.instance.userDAO()
        var tmp_record : Record
        val itemText = EditText(this)
        var itemStr = ""
        when(type){
            MEMBER_INDEX->itemStr = DAO.findMemberByUid(uid)[0].member
            CATEGORY_INDEX->itemStr = DAO.findCategoryByUid(uid)[0].category
            MERCHANT_INDEX->itemStr = DAO.findMerchantByUid(uid)[0].merchant
            ITEM_INDEX->itemStr = DAO.findItemByUid(uid)[0].item
            ACCOUNT_INDEX->itemStr = DAO.findAccountByUid(uid)[0].account
        }
        val origin_txt = itemStr
        itemText.setText(itemStr)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("修改", DialogInterface.OnClickListener{ dialogInterface, i ->
                //不能修改"无"
                if(origin_txt != VOID_ITEM)
                {
                    if(itemText.text.toString() != ""){
                        val txt = itemText.text.toString()
                        when(type){
                            MEMBER_INDEX->{
                                for(records in DAO.findRecordByMember(origin_txt)){
                                    tmp_record = records
                                    tmp_record.member = txt
                                    //修改了member，其他不变
                                    DAO.insertAll(tmp_record)
                                }
                                DAO.insertAllMember(Member(uid,txt))
                            }
                            CATEGORY_INDEX->{
                                for(records in DAO.findRecordByCategory(origin_txt)){
                                    tmp_record = records
                                    tmp_record.category = txt
                                    DAO.insertAll(tmp_record)
                                }
                                DAO.insertAllCategory(Category(uid,txt))
                            }
                            MERCHANT_INDEX->{
                                for(records in DAO.findRecordByMerchant(origin_txt)){
                                    tmp_record = records
                                    tmp_record.merchant = txt
                                    DAO.insertAll(tmp_record)
                                }
                                DAO.insertAllMerchant(Merchant(uid,txt))
                            }
                            ITEM_INDEX->{
                                for(records in DAO.findRecordByItem(origin_txt)){
                                    tmp_record = records
                                    tmp_record.item = txt
                                    DAO.insertAll(tmp_record)
                                }
                                DAO.insertAllItem(Item(uid,txt))
                            }
                            ACCOUNT_INDEX->{
                                for(records in DAO.findRecordByAccount(origin_txt)){
                                    tmp_record = records
                                    tmp_record.account = txt
                                    DAO.insertAll(tmp_record)
                                }
                                DAO.insertAllAccount(Account(uid,txt))
                            }
                        }
                        lastModified[type] = txt
                        InitSpinner()
                        personal_custom_spinner?.setSelection(CUSTOMIZED_LIST.indexOf(typeLabel))

                    }
                }

            })
            .setNegativeButton("删除", DialogInterface.OnClickListener{ dialogInterface, i ->
                //不能删除VOID_ITEM
                if(origin_txt != VOID_ITEM)
                {
                    var txt = ""
                    when(type) {
                        MEMBER_INDEX -> {
                            for(records in DAO.findRecordByMember(origin_txt)){
                                tmp_record = records
                                tmp_record.member = VOID_ITEM
                                //修改了member，其他不变
                                DAO.insertAll(tmp_record)
                            }
                            txt = DAO.findMemberByUid(uid)[0].member
                            DAO.deleteMember(DAO.findMemberByUid(uid)[0])
                        }

                        //删除一级分类的同时也删除二级分类
                        CATEGORY_INDEX -> {
                            for(records in DAO.findRecordByCategory(origin_txt)){
                                tmp_record = records
                                tmp_record.category = VOID_ITEM
                                tmp_record.subcategory = VOID_ITEM
                                DAO.insertAll(tmp_record)
                            }
                            txt = DAO.findCategoryByUid(uid)[0].category
                            DAO.deleteCategory(DAO.findCategoryByUid(uid)[0])
                            //删除一级分类也删除对应二级分类
                            for (sc in DAO.findSubcategoryByCategory(categoryString)){
                                DAO.deleteSubcategory(sc)
                            }
                        }
                        MERCHANT_INDEX ->{
                            for(records in DAO.findRecordByMerchant(origin_txt)){
                                tmp_record = records
                                tmp_record.merchant = VOID_ITEM
                                DAO.insertAll(tmp_record)
                            }
                            txt = DAO.findMerchantByUid(uid)[0].merchant
                            DAO.deleteMerchant(DAO.findMerchantByUid(uid)[0])
                        }
                        ITEM_INDEX -> {
                            for(records in DAO.findRecordByItem(origin_txt)){
                                tmp_record = records
                                tmp_record.item = VOID_ITEM
                                DAO.insertAll(tmp_record)
                            }
                            txt = DAO.findItemByUid(uid)[0].item
                            DAO.deleteItem(DAO.findItemByUid(uid)[0])
                        }
                        //删除账户时删除所有记账
                        ACCOUNT_INDEX -> {
                            for(records in DAO.findRecordByAccount(origin_txt)){
                                DAO.delete(records)
                            }
                            txt = DAO.findAccountByUid(uid)[0].account
                            DAO.deleteAccount(DAO.findAccountByUid(uid)[0])
                        }
                    }
                    if(lastModified[type] == txt)
                        lastModified[type] = ""
                    InitSpinner()
                    personal_custom_spinner?.setSelection(CUSTOMIZED_LIST.indexOf(typeLabel))
                }
            })
            .show()
    }

    
    fun ChangeCustomizedSubcategory(view : View) {
        val title = "请输入二级分类"
        val DAO = AppDatabase.instance.userDAO()
        val itemText = EditText(this)
        var tmp_record : Record
        itemText.setText(DAO.findSubcategoryByUid(subcategory_uid)[0].subcategory)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("修改", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    for(records in DAO.findRecordByCatSubcategory(categoryString,DAO.findSubcategoryByUid(subcategory_uid)[0].subcategory)){
                        tmp_record = records
                        tmp_record.subcategory = itemText.text.toString()
                        DAO.insertAll(tmp_record)
                    }
                    DAO.insertAllSubcategory(Subcategory(subcategory_uid,categoryString,itemText.text.toString()))
                    SubcategorySpinnerAdapt()
                    val subcategoryStringList = mutableListOf<String>()
                    for (subcategorys in DAO.getAllSubcategory(categoryString)) {
                        subcategoryStringList.add(subcategorys.subcategory)
                    }
                    personal_custom_spinner3?.setSelection(subcategoryStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("删除", DialogInterface.OnClickListener{ dialogInterface, i ->
                for(records in DAO.findRecordByCatSubcategory(categoryString,DAO.findSubcategoryByUid(subcategory_uid)[0].subcategory)){
                    tmp_record = records
                    tmp_record.subcategory = VOID_ITEM
                    DAO.insertAll(tmp_record)
                }
                DAO.deleteAccount(DAO.findAccountByUid(uid)[0])


                    DAO.deleteSubcategory(DAO.findSubcategoryByUid(subcategory_uid)[0])
                    SubcategorySpinnerAdapt()
            })
            .show()
    }

    fun ResetPatternPassword(view : View){
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        val isSetPassword:Boolean = false
        val PatternStep:Int = 0
        editor.putBoolean("isSetPassword",isSetPassword)
        editor.putInt("PatternStep",PatternStep)
        editor.commit()
        val intent = Intent()
        intent.setClass(this, PatternPassword::class.java).putExtra(EXTRA_MESSAGE, RESET_PATTERN)
        startActivity(intent)
        finish()
    }

    fun ResetTextPassword(view : View){
        val intent = Intent()
        intent.setClass(this, resetPwd::class.java)
        startActivity(intent)
        finish()
    }


    fun InitThemeSpinner() {
        val themeSpinner = findViewById<Spinner>(R.id.theme_spinner)

        val selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , THEME_LIST)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        themeSpinner.adapter = selectedSpinnerAdapter

        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                val changeTheme = themeColor
                //用于新建/修改Record
                when(adapterView.getItemAtPosition(i) as String){
                    THEME_PURPLE-> themeColor = R.style.CustomizedAppThemePurple
                    THEME_RED-> themeColor = R.style.CustomizedAppThemeRed
                    THEME_PINK-> themeColor = R.style.CustomizedAppThemePink
                    THEME_ORANGE-> themeColor = R.style.CustomizedAppThemeOrange
                    THEME_YELLOW-> themeColor = R.style.CustomizedAppThemeYellow
                    THEME_BLUE-> themeColor = R.style.CustomizedAppThemeBlue
                    THEME_GREEN-> themeColor = R.style.CustomizedAppThemeGreen
                }
                AppDatabase.instance.userDAO().insertAllTheme(Theme(1, themeColor))
                if(changeTheme != themeColor){
                    val intent = Intent()
                    intent.setClass(baseContext , PersonalActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }


    }

    fun logout(view : View){
        MainActivity.isAlreadyLogin = false
        val intent = Intent()
        intent.setClass(this , MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}