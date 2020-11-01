package com.example.paradoxgoverner

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_customization_of_new_item.*
import kotlinx.android.synthetic.main.activity_view_all.*
import kotlin.properties.Delegates

class ViewAllActivity : AppCompatActivity() {

    companion object {
        var instance: ViewAllActivity by Delegates.notNull()
        fun instance() = instance
        var accountFlag = true
        var accountName = VOID_ITEM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_view_all)
        val DAO = AppDatabase.instance.userDAO()
        var totalAmount = 0.0
        for(record in DAO.getAllRecord()){
            if(record.income){
                totalAmount += record.amount
            }
            else{
                totalAmount -= record.amount
            }
        }
        val remainAmountString :String = String.format("%.2f",(totalAmount))
        viewAllAccountSum.setText("总余额"+remainAmountString)

        val accountList = mutableListOf<String>()
        val amountList = mutableListOf<Double>()
        var amount = 0.0
        for(ac in DAO.getAllAccount()){
            if(ac.account != VOID_ITEM){
                accountList.add(ac.account)
                amount = 0.0
                for(record in DAO.findRecordByAccount(ac.account)){
                    if(record.income){
                        amount += record.amount
                    }
                    else{
                        amount -= record.amount
                    }
                }
                amountList.add(amount)
            }
        }

        val viewAllList = findViewById<RecyclerView>(R.id.viewAllRecyclerView)
        viewAllList.layoutManager = LinearLayoutManager(this)
        val myadapter = AccountAdapter(accountList,amountList)
        viewAllList.adapter = myadapter

        val recyclertouchlistener = MainActivity.RecyclerTouchListener(
            this,
            viewAllList,
            object : ClickListener {
                //单击事件  进入Record
                override fun onClick(view: View?, position: Int) {
                    //传递UID，由新Activity去进行查询
                    accountFlag = true
                    accountName = accountList.get(position)
                    val intent = Intent(this@ViewAllActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onLongClick(view: View?, position: Int) {
                }
            }
        )
        //onClick
        viewAllList.addOnItemTouchListener(recyclertouchlistener)

    }

    fun backToMain(view : View){
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }



}