package com.example.paradoxgoverner

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reset_pwd.*

class resetPwd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(PersonalActivity.themeColor)
        setContentView(R.layout.activity_reset_pwd)
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        Toast.makeText(this,"请输入以前的密码",Toast.LENGTH_SHORT).show()
        val passwordEdit = findViewById<EditText>(R.id.password)
        //0:输入以前密码
        //1：第一次输入密码
        //2：第二次输入密码
        var step = 0
        var new_pwd1 = ""
        var new_pwd2 = ""
        passwordEdit.hint = "请输入原密码"
        reset.setOnClickListener {
            if(step == 0){
                passwordEdit.hint = "请输入新密码"
                val pwd1 = passwordEdit.text.toString()
                if(isPwdLegal(pwd1)){
                    val save_password = settings.getString("save_password","").toString()
                    if(pwd1==save_password){
                        Toast.makeText(this,"密码正确,请输入新的密码",Toast.LENGTH_SHORT).show()
                        passwordEdit.setText("")
                        step = 1
                    }
                    else{
                        Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"密码长度需大于4",Toast.LENGTH_SHORT).show()
                }
            }else if(step == 1){
                passwordEdit.hint = "请确认新密码"
                new_pwd1 = passwordEdit.text.toString()
                if(isPwdLegal(new_pwd1)){
                    Toast.makeText(this,"请重复密码",Toast.LENGTH_SHORT).show()
                    passwordEdit.setText("")
                    step = 2
                }else{
                    Toast.makeText(this,"密码长度需大于4",Toast.LENGTH_SHORT).show()
                }
            }else if(step==2){

                new_pwd2 = passwordEdit.text.toString()
                if(isPwdLegal(new_pwd2)){
                    if(new_pwd2 == new_pwd1){
                        editor.putString("save_password",new_pwd1)
                        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        //在修改密码后跳转至Personal，在登录后跳转至Main
                        if(MainActivity.isAlreadyLogin){
                            intent.setClass(this@resetPwd, PersonalActivity::class.java)
                        }
                        else{
                            intent.setClass(this@resetPwd, MainActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"两次密码不相同,请重新输入第二次密码",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"密码长度需大于4",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //密码长度大于4
    fun isPwdLegal(pwd:String):Boolean {
        return pwd.length > 4
    }

    fun onClick(passwordEdit: EditText, password2Edit: EditText) {
        val password: String = passwordEdit.text.toString()
        val password2: String = password2Edit.text.toString()
        doReset(password, password2)
    }
    fun doReset(password: String, password2: String) {
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        if (password == password2) {
            editor.putString("save_password", password)
            editor.commit()
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.setClass(this, PersonalActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show()
        }
    }

}