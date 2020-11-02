package com.example.paradoxgoverner

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val passwordEdit = findViewById<EditText>(R.id.password)
        val password2Edit = findViewById<EditText>(R.id.password2)
        mRigisterBtn.setOnClickListener {
            onClick(passwordEdit,password2Edit)
        }
    }
    fun onClick(passwordEdit:EditText,password2Edit: EditText) {
        val password: String = passwordEdit.text.toString()
        val password2: String = password2Edit.text.toString()
        if(isPwdLegal(password) && isPwdLegal(password2)){
            doRegister(password, password2)
        }else{
            Toast.makeText(this,"密码长度需大于4", Toast.LENGTH_SHORT).show()
        }

    }
    fun doRegister(password:String,password2:String){
        if(password==password2){
            val settings: SharedPreferences = getSharedPreferences("info", 0)
            val editor = settings.edit()
            val isAlreadyRegister = true
            editor.putBoolean("isAlreadyRegister", isAlreadyRegister)
            editor.putString("save_password",password)
            editor.commit()
            Toast.makeText(this,"注册成功,请设置手势密码", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.setClass(this,PatternPassword::class.java)
            startActivity(intent)
            finish()
        }else if(password!=password2){
            Toast.makeText(this,"密码错误", Toast.LENGTH_SHORT).show()
        }
    }
    //密码长度大于4
    fun isPwdLegal(pwd:String):Boolean{
        return pwd.length > 4
    }
}