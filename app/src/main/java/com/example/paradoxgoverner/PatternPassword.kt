package com.example.paradoxgoverner

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import kotlinx.android.synthetic.main.activity_pattern_password.*

class PatternPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern_password)
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var save_password9:String ?= settings.getString("password9", null)
        var isSetPassword:Boolean = settings.getBoolean("isSetPassword",false)
        var password9:String ?=null
        pattern_lock_view.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {
                //根据需要添加业务逻辑
            }

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {
                //根据需要添加业务逻辑
            }

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                //根据需要添加业务逻辑
                if(!isSetPassword){
                    isSetPassword = true
                    password9 = hitIndexList.toString()
                    editor.putBoolean("isSetPassword",isSetPassword)
                    editor.putString("password9",password9)
                    editor.commit()
                    Toast.makeText(this@PatternPassword, "密码设置成功,为："+hitIndexList.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.setClass(this@PatternPassword, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    if(save_password9 == hitIndexList.toString()){
                        Toast.makeText(this@PatternPassword, "密码正确", Toast.LENGTH_SHORT).show()
                        var isAlreadyLogin = true
                        editor.putBoolean("isAlreadyLogin", isAlreadyLogin)
                        editor.commit()
                        val intent = Intent()
                        intent.setClass(this@PatternPassword, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@PatternPassword, "密码错误", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onClear(view: PatternLockerView) {
                //根据需要添加业务逻辑

            }
        })
    }
}