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
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_pattern_password)
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var save_password9:String ?= settings.getString("password9", null)
        var isSetPassword:Boolean = settings.getBoolean("isSetPassword",false)
        var password9_0:String ?=null
        var password9_1:String ?=null
        var password9_2:String ?=null
        //0：输入之前的密码
        //1：输入新密码
        //2：重复新密码
        var step = settings.getInt("PatternStep",1)
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
                    if(step==0){
                        password9_0 = hitIndexList.toString()
                        if(password9_0 == save_password9){
                            Toast.makeText(this@PatternPassword, "请输入新密码", Toast.LENGTH_SHORT).show()
                            step = 1
                        }else{
                            Toast.makeText(this@PatternPassword, "密码错误", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else if(step==1){
                        password9_1 = hitIndexList.toString()
                        step = 2
                        Toast.makeText(this@PatternPassword, "请重复密码", Toast.LENGTH_SHORT).show()
                    }
                    else if(step==2){
                        password9_2 = hitIndexList.toString()
                        if(password9_1 == password9_2){
                            isSetPassword = true
                            editor.putBoolean("isSetPassword",isSetPassword)
                            editor.putInt("PatternStep",1)
                            editor.putString("password9",password9_2)
                            editor.commit()
                            Toast.makeText(this@PatternPassword, "密码设置成功,为："+hitIndexList.toString(), Toast.LENGTH_SHORT).show()
                            val intent = Intent()
                            //在修改密码后跳转至Personal，在登录后跳转至Main
                            if(MainActivity.isAlreadyLogin){
                            intent.setClass(this@PatternPassword, PersonalActivity::class.java)
                            }
                            else{
                                intent.setClass(this@PatternPassword, MainActivity::class.java)
                            }
                            startActivity(intent)
                            finish()
                        }else {
                            Toast.makeText(this@PatternPassword, "第二次密码错误，请重复第二次密码", Toast.LENGTH_SHORT).show()
                        }
                    }

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