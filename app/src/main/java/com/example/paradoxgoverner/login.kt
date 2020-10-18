package com.example.paradoxgoverner

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {
    val dataBase:userNameAndPwdDB = userNameAndPwdDB.getDatabase(this)
    val UserAndPasswordDAO:userNameAndPwdDAO = dataBase.UserNameAndPwdDAO()
    var setAutoLogin:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val userNameEdit = findViewById<EditText>(R.id.userName)
        val passwordEdit = findViewById<EditText>(R.id.password)
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var autoLogin:Boolean = settings.getBoolean("autoLogin", false)
        var isAlreadyLogin:Boolean = settings.getBoolean("isAlreadyLogin", false)
        usePattern.setOnClickListener{
            onClick2()
        }
        checkBox1.setOnCheckedChangeListener{ checkBox1, isCheck->if(isCheck){
            setAutoLogin = true}else {
            setAutoLogin = false
        }
        }
        if(autoLogin){
            isAlreadyLogin = true
            editor.putBoolean("isAlreadyLogin", isAlreadyLogin)
            editor.commit()
            val intent = Intent()
            intent.setClass(this, MainActivity::class.java)
            startActivity(intent)
        }else {
            mLoginBtn.setOnClickListener {
                onClick(userNameEdit, passwordEdit)
            }
        }

    }
    fun onClick(userNameEdit: EditText, passwordEdit: EditText) {
        val userName: String = userNameEdit.text.toString()
        val password: String = passwordEdit.text.toString()
        doLogin(userName, password)
    }

    private fun doLogin(userName: String, password: String){
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        var autoLogin:Boolean = settings.getBoolean("autoLogin", false)
        var isAlreadyLogin:Boolean = settings.getBoolean("isAlreadyLogin", false)
        if(UserAndPasswordDAO.searchNameAndPwd(userName)){
            if(UserAndPasswordDAO.searchPwdByName(userName) == password){
                editor.putBoolean("autoLogin", setAutoLogin)
                isAlreadyLogin = true
                editor.putBoolean("isAlreadyLogin", isAlreadyLogin)
                editor.commit()
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.setClass(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "账号不存在", Toast.LENGTH_SHORT).show()
        }
    }
    fun onClick2(){
        val intent = Intent()
        intent.setClass(this,PatternPassword::class.java)
        startActivity(intent)
    }
}