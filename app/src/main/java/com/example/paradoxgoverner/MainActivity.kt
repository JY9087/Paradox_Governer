package com.example.paradoxgoverner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(){

    //静态内部类：伴生对象
    companion object {
        var instance:  MainActivity by Delegates.notNull()
        fun instance() = instance
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Room
        instance = this
        val DAO : UserDAO = AppDatabase.instance.userDAO()

        //一次性初始化 , if判断
        if(DAO.isInitialized().size == 0)
        {
            DAO.initialize(Hidden(0))
            for (init_member in DEFAULT_MEMBER_LIST){
                DAO.insertAllMember(Member(0,init_member))
            }
            for (init_category in DEFAULT_CATEGORY_LIST){
                DAO.insertAllCategory(Category(0,init_category))
            }
            //until不包含最后一个元素
            for (index in 0 until DEFAULT_CATEGORY_LIST.size){
                for(item in DEFAULT_SUBCATEGORY_LIST.get(index))
                {
                    DAO.insertAllSubcategory(Subcategory(0, DEFAULT_CATEGORY_LIST.get(index),item))
                }
            }
        }

        //RecycleView
        val forecastList = findViewById<RecyclerView>(R.id.forecast)
        forecastList.layoutManager = LinearLayoutManager(this)
        val myadapter = ForecastListAdapter(DAO.getAll())
        forecastList.adapter = myadapter


        var recyclertouchlistener = RecyclerTouchListener(
            this,
            forecastList,
            object : ClickListener{
                //单击事件  进入Record
                override fun onClick(view: View?, position: Int)
                {
                    //传递UID，由新Activity去进行查询
                    //Todo : 改为传递Record
                    val intent = Intent(MainActivity.instance, CreateNewItem::class.java).putExtra(
                        RECORD_UID,DAO.getAll().get(position).uid)
                    startActivity(intent)

                }
                override fun onLongClick(view: View?, position: Int)
                {

                }
            }
        )
        //onClick
        forecastList.addOnItemTouchListener(recyclertouchlistener)




        // The method returns a MaterialDrawable, but as it is private to the builder you'll have to store it as a regular Drawable ;)
        var GraphButtonDrawable = MaterialDrawableBuilder.with(this) // provide a context
            .setIcon(MaterialDrawableBuilder.IconValue.CHART_PIE) // provide an icon
            .setColor(R.color.colorPrimary) // set the icon color
            .setToActionbarSize() // set the icon size
            .build() // Finally call build

        var GraphButton = findViewById<Button>(R.id.graph_button)
        GraphButton.setCompoundDrawables(GraphButtonDrawable,null,null,null)

        var StatisticsButtonDrawable = MaterialDrawableBuilder.with(this) // provide a context
            .setIcon(MaterialDrawableBuilder.IconValue.POLL_BOX) // provide an icon
            .setColor(R.color.colorPrimary) // set the icon color
            .setToActionbarSize() // set the icon size
            .build() // Finally call build

        var StatisticsButton = findViewById<Button>(R.id.statistics_button)
        StatisticsButton.setCompoundDrawables(StatisticsButtonDrawable,null,null,null)

        var AccountInfoButtonDrawable = MaterialDrawableBuilder.with(this) // provide a context
            .setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT) // provide an icon
            .setColor(R.color.colorPrimary) // set the icon color
            .setToActionbarSize() // set the icon size
            .build() // Finally call build

        var AccountInfoButton = findViewById<Button>(R.id.account_info_button)
        AccountInfoButton.setCompoundDrawables(AccountInfoButtonDrawable,null,null,null)

        var NewRecordButtonDrawable = MaterialDrawableBuilder.with(this) // provide a context
            .setIcon(MaterialDrawableBuilder.IconValue.BOOKMARK_PLUS_OUTLINE) // provide an icon
            .setColor(R.color.colorPrimary) // set the icon color
            .setToActionbarSize() // set the icon size
            .build() // Finally call build

        var NewRecordButton = findViewById<Button>(R.id.new_record_button)
        NewRecordButton.setCompoundDrawables(NewRecordButtonDrawable,null,null,null)
    }
    //End Of OnCreate

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






