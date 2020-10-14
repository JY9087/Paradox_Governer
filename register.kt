package com.example.paradoxgoverner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class register : AppCompatActivity() {
    val dataBase:userNameAndPwdDB = userNameAndPwdDB.getDatabase(this)
    val UserAndPasswordDAO:userNameAndPwdDAO = dataBase.UserNameAndPwdDAO()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val userNameEdit = findViewById<EditText>(R.id.userName)
        val passwordEdit = findViewById<EditText>(R.id.password)
        val password2Edit = findViewById<EditText>(R.id.password2)


        mRigisterBtn.setOnClickListener {
            onClick(userNameEdit,passwordEdit,password2Edit)
        }
    }
    fun onClick(userNameEdit:EditText,passwordEdit:EditText,password2Edit: EditText) {
        val userName: String = userNameEdit.text.toString()
        val password: String = passwordEdit.text.toString()
        val password2: String = password2Edit.text.toString()
        doRegister(userName, password, password2)
    }
    fun doRegister(userName:String,password:String,password2:String){
        if(password==password2 && !UserAndPasswordDAO.searchNameAndPwd(userName)){
            var temp:userNameAndPwd = userNameAndPwd(1,userName,password)
            UserAndPasswordDAO.insertNameAndPwd(temp)
            Toast.makeText(this,"注册成功", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.setClass(this,MainActivity::class.java)
            startActivity(intent)

        }else if(password!=password2){
            Toast.makeText(this,"密码错误", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"账号已存在", Toast.LENGTH_SHORT).show()
        }
    }
}