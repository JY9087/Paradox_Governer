package com.example.paradoxgoverner

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_customization_of_new_item.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    //静态内部类：伴生对象
    companion object {
        var instance: MainActivity by Delegates.notNull()
        fun instance() = instance
        var isAlreadyLogin = false
        var versionFlag = false
    }

    var accountName = ALL_ACCOUNT
    var income = true
    var accountStringList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_main)

        //判断版本
        val version = Integer.valueOf(Build.VERSION.SDK)
        if (version >= 5) {
            versionFlag = true
        }

        //Room
        instance = this
        val DAO: UserDAO = AppDatabase.instance.userDAO()

        //一次性初始化 , if判断
        if (DAO.isInitialized().size == 0) {
            DAO.initialize(Hidden(0))
            for (init_member in DEFAULT_MEMBER_LIST) {
                DAO.insertAllMember(Member(0, init_member))
            }

            for (init_category in DEFAULT_CATEGORY_LIST) {
                DAO.insertAllCategory(Category(0, init_category))
            }

            //until不包含最后一个元素
            for (index in 0 until DEFAULT_CATEGORY_LIST.size) {
                for (item in DEFAULT_SUBCATEGORY_LIST.get(index)) {
                    DAO.insertAllSubcategory(Subcategory(0, DEFAULT_CATEGORY_LIST.get(index), item))
                }
            }

            for (init_merchant in DEFAULT_MERCHANT_LIST) {
                DAO.insertAllMerchant(Merchant(0, init_merchant))
            }

            for (init_item in DEFAULT_ITEM_LIST) {
                DAO.insertAllItem(Item(0, init_item))
            }

            for (init_account in DEFAULT_ACCOUNT_LIST) {
                DAO.insertAllAccount(Account(0, init_account))
            }
        }


        InitAccountSpinner()





        //用户名和密码数据库，及用于判断是进入注册界面还是登录界面还是直接进入主界面的变量
        //使用OnCreate的局部变量？有待商榷。看看是否需要修改
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var isAlreadyRegister:Boolean = settings.getBoolean("isAlreadyRegister",false)
        //已经注册过，进入登录界面

        //尚未注册
        if(isAlreadyRegister) {
            //尚未登录：前往登录
            if(!isAlreadyLogin){
                val intent = Intent()
                intent.setClass(this, Login::class.java)
                startActivity(intent)
                isAlreadyLogin = true
                finish()
            }
        }else{//尚未注册，进入注册界面
            val intent = Intent()
            intent.setClass(this, Register::class.java)
            startActivity(intent)
            finish()
        }
        //上述注册和登录完成



        var bottomNavigatior = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigatior.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {}
                R.id.navigation_dashboard-> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_graph -> {
                    val intent = Intent(this, GraphActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
                R.id.navigation_personal -> {
                    val intent = Intent(this, PersonalActivity::class.java)
                    startActivity(intent)
                    if (versionFlag) {
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout)
                    }
                    finish()
                }
            }
            true
        })
        bottomNavigatior.selectedItemId = R.id.navigation_home


        //RecycleView
        val forecastList = findViewById<RecyclerView>(R.id.forecast)
        forecastList.layoutManager = LinearLayoutManager(this)
        var myadapter = ForecastListAdapter(statisticsActivity.recordList2)

        /*
        if( ! statisticsActivity.searchFlag){
            myadapter = ForecastListAdapter(DAO.getAllRecord())
        }
        statisticsActivity.searchFlag = false
         */
        forecastList.adapter = myadapter

        var recyclertouchlistener = RecyclerTouchListener(
            this,
            forecastList,
            object : ClickListener {
                //单击事件  进入Record
                override fun onClick(view: View?, position: Int) {
                    //传递UID，由新Activity去进行查询
                    val intent = Intent(instance, CreateNewItem::class.java).putExtra(
                        RECORD_UID, DAO.getAllRecord().get(position).uid
                    )
                    startActivity(intent)
                    finish()
                }
                override fun onLongClick(view: View?, position: Int) {
                    wantToDelete(DAO.getAllRecord().get(position).uid)
                }
            }
        )
        //onClick
        forecastList.addOnItemTouchListener(recyclertouchlistener)
    }
    //End Of OnCreate

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_type_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val forecastList = findViewById<RecyclerView>(R.id.forecast)
        val DAO = AppDatabase.instance.userDAO()
        var r = "收入"
        when (item.itemId) {
            R.id.navigation_all -> r = "全部"
            R.id.navigation_income -> r = "收入"
            R.id.navigation_outlay -> r = "支出"
            R.id.navigation_loan -> r = "借贷"
            R.id.navigation_transfer -> r = "转账"
            else -> super.onOptionsItemSelected(item)
        }
        if(r == "全部") {
            //forecastList.adapter = ForecastListAdapter(DAO.getAllRecord())
        }
        else{
            //forecastList.adapter = ForecastListAdapter(DAO.findRecordByType(r))
        }

        return true
    }

    //从虚拟类OnItemTouchListener实例化而来
    class RecyclerTouchListener(
        context: Context?,
        recycleView: RecyclerView,
        val clicklistener: ClickListener?
    ) :
        RecyclerView.OnItemTouchListener {

        //手势侦测
        private val gestureDetector: GestureDetector

        //拦截触摸事件
        override fun onInterceptTouchEvent(
            rv: RecyclerView,
            e: MotionEvent
        ): Boolean {
            //通过坐标找到ChildView
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                //通过接口调用单击函数
                clicklistener.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    //单击
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }

                    //长按
                    override fun onLongPress(e: MotionEvent) {
                        val child =
                            recycleView.findChildViewUnder(e.x, e.y)
                        if (child != null && clicklistener != null) {
                            clicklistener.onLongClick(
                                child,
                                recycleView.getChildAdapterPosition(child)
                            )
                        }
                    }
                })
        }
    }


    fun CreateNewItem(view: View) {
        val intent = Intent(this, CreateNewItem::class.java)
        startActivity(intent)
        finish()
    }


    fun wantToDelete(uid: Int){
        val DAO = AppDatabase.instance.userDAO()
        AlertDialog.Builder(this)
            .setTitle("确认删除？")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("确定", DialogInterface.OnClickListener{ dialogInterface, i ->
                DAO.delete( DAO.findRecordByUid(uid) )
                //findViewById<RecyclerView>(R.id.forecast).adapter = ForecastListAdapter(DAO.getAllRecord())
            })
            .setNegativeButton("取消", null)
            .show()
    }



    fun InitAccountSpinner() {
        val DAO = AppDatabase.instance.userDAO()
        accountStringList.clear()
        accountStringList.add(ALL_ACCOUNT)
        for (accounts in DAO.getAllAccount()) {
            if(accounts.account != VOID_ITEM){
                accountStringList.add(accounts.account)
            }
        }
        var AccountNameText = findViewById<TextView>(R.id.AccountNameText)
        AccountNameText.text = "全部账户"

        var AccountInfo = findViewById<TextView>(R.id.AccountInfoText)
        //todo : 使用真正余额
        AccountInfo.text = "余额："

        var selectedSpinner = findViewById<Spinner>(R.id.AccountSpinner)
        var selectedSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item , accountStringList.toList())
        selectedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        selectedSpinner.setAdapter(selectedSpinnerAdapter)

        selectedSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                i: Int,
                l: Long
            ) {
                accountName = adapterView.getItemAtPosition(i) as String
                val forecastList = findViewById<RecyclerView>(R.id.forecast)
                if(accountName == ALL_ACCOUNT) {
                    //var myadapter = ForecastListAdapter(DAO.getAllRecord())
                    //forecastList.adapter = myadapter
                    AccountNameText.text = "全部账户"
                }
                else{
                    //var myadapter = ForecastListAdapter(DAO.findRecordByAccount(accountName))
                    //forecastList.adapter = myadapter
                    AccountNameText.text = "当前账户：" + accountName
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(applicationContext, "No selection", Toast.LENGTH_LONG).show()
            }
        })
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
                    InitAccountSpinner()
                    item_spinner?.setSelection(accountStringList.indexOf(itemText.text.toString()))
                }
            })
            .setNegativeButton("取消", null)
            .show()
    }

}

interface ClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}