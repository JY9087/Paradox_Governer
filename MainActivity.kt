package com.example.paradoxgoverner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(){

    //静态内部类：伴生对象
    //为什么这里要多一个instance()函数？
    companion object {
        var instance:  MainActivity by Delegates.notNull()
        fun instance() = instance
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Room
        //record数据库
        instance = this
        val DAO : UserDAO = AppDatabase.instance.userDAO()
        //用户名和密码数据库，及用于判断是进入注册界面还是登录界面还是直接进入主界面的变量
        val dataBase:userNameAndPwdDB = userNameAndPwdDB.getDatabase(this)
        val UserAndPasswordDAO:userNameAndPwdDAO = dataBase.UserNameAndPwdDAO()
        val userList:List<userNameAndPwd> = UserAndPasswordDAO.findall()
        var isAlreadyRegister:Boolean = if (userList.size==0) false else true

        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var isAlreadyLogin:Boolean = settings.getBoolean("isAlreadyLogin", false)
        //RecycleView
        val forecastList = findViewById<RecyclerView>(R.id.forecast)
        forecastList.layoutManager = LinearLayoutManager(this)
        val myadapter = ForecastListAdapter(DAO.getAll())
        forecastList.adapter = myadapter
        //已经注册过，进入登录界面
        if(isAlreadyRegister) {
            //已经登录了，进入主界面
            if(!isAlreadyLogin){
                val intent = Intent()
                intent.setClass(this, login::class.java)
                startActivity(intent)
            }
        }else{//尚未注册，进入注册界面
            val intent = Intent()
            intent.setClass(this, register::class.java)
            startActivity(intent)
        }
        isAlreadyLogin = false
        editor.putBoolean("isAlreadyLogin",isAlreadyLogin)
        editor.commit()
        //上述注册和登录完成


        var recyclertouchlistener = RecyclerTouchListener(
            this,
            forecastList,
            object : ClickListener{
                override fun onClick(view: View?, position: Int)
                {
                    Toast.makeText(MainActivity.instance(), "Single Click on position :"+position,
                        Toast.LENGTH_SHORT).show();
                }
                override fun onLongClick(view: View?, position: Int)
                {
                    Toast.makeText(MainActivity.instance(), "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
                }
            }
        )
        //onClick
        forecastList.addOnItemTouchListener(recyclertouchlistener)
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
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                //单击
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                //长按
                override fun onLongPress(e: MotionEvent) {
                    val child =
                        recycleView.findChildViewUnder(e.x, e.y)
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child))
                    }
                }
            })
        }
    }



    fun CreateNewItem(view:View)
    {
        val intent = Intent(this, CreateNewItem::class.java)
        startActivity(intent)
    }


}

interface ClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}




