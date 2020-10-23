package com.example.paradoxgoverner

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
    }

    var stringList = mutableListOf<String>()
    var type = MEMBER_INDEX
    var uid = 0
    var subcategory_uid = 0
    var categoryString = DEFAULT_CATEGORY_LIST[0]

    var lastModified = mutableListOf<String>("","","","","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)

        var bottomNavigatior = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigatior.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                }
                R.id.navigation_dashboard-> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                }
                R.id.navigation_graph -> {
                    val intent = Intent(this, GraphActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                }
                R.id.navigation_personal -> {}
            }
            true
        })
        bottomNavigatior.selectedItemId = R.id.navigation_personal

        InitSpinner()
    }

    fun InitSpinner() {
        var selectedSpinner = findViewById<Spinner>(R.id.personal_custom_spinner)
        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , CUSTOMIZED_LIST)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectedSpinner.setAdapter(selectedSpinnerAdapter)
        val DAO = AppDatabase.instance.userDAO()
        selectedSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                //kotlin list下标从0开始
                when(adapterView.getItemAtPosition(i) as String){
                    CUSTOMIZED_LIST[0] -> {
                        type = MEMBER_INDEX
                        stringList.clear()
                        for (members in DAO.getAllMember()) {
                            stringList.add(members.member)
                        }
                        InitSecondSpinner(stringList)
                        personal_custom_spinner3.visibility = View.INVISIBLE
                        personal_custom_button3.visibility = View.INVISIBLE
                        personal_custom_button4.visibility = View.INVISIBLE
                    }

                    CUSTOMIZED_LIST[1] -> {
                        type = CATEGORY_INDEX
                        stringList.clear()
                        for (categorys in DAO.getAllCategory()) {
                            stringList.add(categorys.category)
                        }
                        InitSecondSpinner(stringList)
                        personal_custom_spinner3.visibility = View.VISIBLE
                        personal_custom_button3.visibility = View.VISIBLE
                        personal_custom_button4.visibility = View.VISIBLE
                    }

                    CUSTOMIZED_LIST[2] -> {
                        type = MERCHANT_INDEX
                        stringList.clear()
                        for (merchants in DAO.getAllMerchant()) {
                            stringList.add(merchants.merchant)
                        }
                        InitSecondSpinner(stringList)
                        personal_custom_spinner3.visibility = View.INVISIBLE
                        personal_custom_button3.visibility = View.INVISIBLE
                        personal_custom_button4.visibility = View.INVISIBLE
                    }

                    CUSTOMIZED_LIST[3] -> {
                        type = ITEM_INDEX
                        stringList.clear()
                        for (items in DAO.getAllItem()) {
                            stringList.add(items.item)
                        }
                        InitSecondSpinner(stringList)
                        personal_custom_spinner3.visibility = View.INVISIBLE
                        personal_custom_button3.visibility = View.INVISIBLE
                        personal_custom_button4.visibility = View.INVISIBLE
                    }

                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun InitSecondSpinner(itemlist : List<String>) {
        var selectedSpinner = findViewById<Spinner>(R.id.personal_custom_spinner2)

        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , itemlist)
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
        if(lastModified[type] != "")
            personal_custom_spinner2?.setSelection(stringList.indexOf(lastModified[type]))
    }

    fun SubcategorySpinnerAdapt() {
        val DAO = AppDatabase.instance.userDAO()
        var subcategoryList = DAO.getAllSubcategory(categoryString)
        var subcategoryStringList = mutableListOf<String>()
        for (subcategorys in subcategoryList) {
            subcategoryStringList.add(subcategorys.subcategory)
        }
        var subcategoryspinner = findViewById<Spinner>(R.id.personal_custom_spinner3)

        val subcategoryadapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , subcategoryStringList.toList())
        subcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcategoryspinner.setAdapter(subcategoryadapter)

        subcategoryspinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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
        })
    }

    fun NewCustomizedItem(view : View) {
        var title = "请输入"
        var typeLabel =""
        //这东西有潜在风险。不该这样用String
        when(type){
            MEMBER_INDEX->typeLabel = "成员"
            CATEGORY_INDEX->typeLabel = "一级分类"
            MERCHANT_INDEX->typeLabel = "商家"
            ITEM_INDEX->typeLabel = "项目"
        }
        title += typeLabel
        val DAO = AppDatabase.instance.userDAO()
        var itemText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != "") {
                    val txt = itemText.text.toString()
                    when (type) {
                        MEMBER_INDEX -> DAO.insertAllMember(Member(0, txt))
                        CATEGORY_INDEX -> DAO.insertAllCategory(Category(0, txt))
                        MERCHANT_INDEX -> DAO.insertAllMerchant(Merchant(0, txt))
                        ITEM_INDEX -> DAO.insertAllItem(Item(0, txt))
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
        var title = "请输入二级分类"
        val DAO = AppDatabase.instance.userDAO()
        var itemText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    DAO.insertAllSubcategory(Subcategory(0,categoryString,itemText.text.toString()))
                    SubcategorySpinnerAdapt()
                    var subcategoryStringList = mutableListOf<String>()
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
        //这东西有潜在风险。不该这样用String
        when(type){
            MEMBER_INDEX->typeLabel = "成员"
            CATEGORY_INDEX->typeLabel = "一级分类"
            MERCHANT_INDEX->typeLabel = "商家"
            ITEM_INDEX->typeLabel = "项目"
        }
        title += typeLabel
        val DAO = AppDatabase.instance.userDAO()
        var itemText = EditText(this)
        var itemStr = ""
        when(type){
            MEMBER_INDEX->itemStr = DAO.findMemberByUid(uid)[0].member
            CATEGORY_INDEX->itemStr = DAO.findCategoryByUid(uid)[0].category
            MERCHANT_INDEX->itemStr = DAO.findMerchantByUid(uid)[0].merchant
            ITEM_INDEX->itemStr = DAO.findItemByUid(uid)[0].item
        }
        itemText.setText(itemStr)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("修改", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    val txt = itemText.text.toString()
                    when(type){
                        MEMBER_INDEX->DAO.insertAllMember(Member(uid,txt))
                        CATEGORY_INDEX->DAO.insertAllCategory(Category(uid,txt))
                        MERCHANT_INDEX->DAO.insertAllMerchant(Merchant(uid,txt))
                        ITEM_INDEX->DAO.insertAllItem(Item(uid,txt))
                    }
                    lastModified[type] = txt
                    InitSpinner()
                    personal_custom_spinner?.setSelection(CUSTOMIZED_LIST.indexOf(typeLabel))

                }
            })
            .setNegativeButton("删除", DialogInterface.OnClickListener{ dialogInterface, i ->
                var txt = ""
                when(type) {
                    MEMBER_INDEX -> {
                        txt = DAO.findMemberByUid(uid)[0].member
                        DAO.deleteMember(DAO.findMemberByUid(uid)[0])
                    }
                    CATEGORY_INDEX -> {
                        txt = DAO.findCategoryByUid(uid)[0].category
                        DAO.deleteCategory(DAO.findCategoryByUid(uid)[0])
                        for (sc in DAO.findSubcategoryByCategory(categoryString)){
                            DAO.deleteSubcategory(sc)
                        }
                    }
                    MERCHANT_INDEX ->{
                        txt = DAO.findMerchantByUid(uid)[0].merchant
                        DAO.deleteMerchant(DAO.findMerchantByUid(uid)[0])
                    }
                    ITEM_INDEX -> {
                        txt = DAO.findItemByUid(uid)[0].item
                        DAO.deleteItem(DAO.findItemByUid(uid)[0])
                    }
                }
                if(lastModified[type] == txt)
                    lastModified[type] = ""
                InitSpinner()
                personal_custom_spinner?.setSelection(CUSTOMIZED_LIST.indexOf(typeLabel))

            })
            .show()
    }

    fun ChangeCustomizedSubcategory(view : View) {
        var title = "请输入二级分类"
        val DAO = AppDatabase.instance.userDAO()
        var itemText = EditText(this)
        itemText.setText(DAO.findSubcategoryByUid(subcategory_uid)[0].subcategory)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setView(itemText)
            .setPositiveButton("修改", DialogInterface.OnClickListener{ dialogInterface, i ->
                if(itemText.text.toString() != ""){
                    DAO.insertAllSubcategory(Subcategory(subcategory_uid,categoryString,itemText.text.toString()))
                    SubcategorySpinnerAdapt()
                    var subcategoryStringList = mutableListOf<String>()
                    for (subcategorys in DAO.getAllSubcategory(categoryString)) {
                        subcategoryStringList.add(subcategorys.subcategory)
                    }
                    personal_custom_spinner3?.setSelection(subcategoryStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("删除", DialogInterface.OnClickListener{ dialogInterface, i ->
                    DAO.deleteSubcategory(DAO.findSubcategoryByUid(subcategory_uid)[0])
                    SubcategorySpinnerAdapt()
            })
            .show()
    }

}