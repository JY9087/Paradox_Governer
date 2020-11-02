package com.example.paradoxgoverner

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user_center.*

class userCenter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_center)
        resetPattern.setOnClickListener {
            onClick1()
        }
        resetPwd.setOnClickListener {
            onClick2()
        }
    }
    fun onClick1(){
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var isSetPassword:Boolean = false
        editor.putBoolean("isSetPassword",isSetPassword)
        editor.putInt("PatternStep",0)
        editor.commit()
        val intent = Intent()
        intent.setClass(this, PatternPassword::class.java)
        startActivity(intent)
        finish()
    }
    fun onClick2(){
        val PatternStep = 0;
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        editor.putInt("PatternStep",0)
        editor.commit()
        val intent = Intent()
        intent.setClass(this, com.example.paradoxgoverner.resetPwd::class.java)
        startActivity(intent)
        finish()
    }
}
