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
        setContentView(R.layout.activity_reset_pwd)
        val passwordEdit = findViewById<EditText>(R.id.password)
        val password2Edit = findViewById<EditText>(R.id.password2)
        reset.setOnClickListener {
            onClick(passwordEdit,password2Edit)
        }
    }
    fun onClick(passwordEdit:EditText,password2Edit: EditText) {
        val password: String = passwordEdit.text.toString()
        val password2: String = password2Edit.text.toString()
        doReset(password, password2)
    }
    fun doReset(password:String,password2:String){
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()
        if(password==password2){
            editor.putString("save_password",password)
            editor.commit()
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.setClass(this, PersonalActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show()
        }
    }
}