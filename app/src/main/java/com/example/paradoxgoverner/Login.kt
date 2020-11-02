package com.example.paradoxgoverner

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    var fingerprintHelper: FingerprintHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val passwordEdit = findViewById<EditText>(R.id.password)
        val settings: SharedPreferences = getSharedPreferences("info", 0)
        val editor = settings.edit()

        usePattern.setOnClickListener{
            onClick2()
        }

        useFinger.setOnClickListener{
            onClick3()
        }

        var rememberPwd = settings.getBoolean("rememberPwd",false)
        if(rememberPwd){
            var pwd = settings.getString("save_password","").toString()
            passwordEdit.setText(pwd)
            checkBox1.setChecked(true)
        }
        mLoginBtn.setOnClickListener {
            var password = passwordEdit.text.toString()
            var save_password = settings.getString("save_password","").toString()
            if(isPwdLegal(password)){
                if(password == save_password){
                    if(checkBox1.isChecked){
                        editor.putBoolean("rememberPwd",true)
                        editor.commit()
                    }
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.setClass(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"密码长度需大于4",Toast.LENGTH_SHORT).show()
            }
        }

    }



    fun onClick2(){
        val intent = Intent()
        intent.setClass(this,PatternPassword::class.java)
        startActivity(intent)
        finish()
    }
    fun onClick3(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val settings: SharedPreferences = getSharedPreferences("info", 0)
            val editor = settings.edit()
            var isAlreadyLogin:Boolean = settings.getBoolean("isAlreadyLogin", false)
            fingerprintHelper = FingerprintHelper(this)
            fingerprintHelper?.setAuthenticationCallback(object : FingerprintHelper.AuthenticationCallback {
                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(this@Login, "解锁失败", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(value: String) {
                    Toast.makeText(this@Login, "解锁成功", Toast.LENGTH_SHORT).show()
                    isAlreadyLogin = true
                    editor.putBoolean("isAlreadyLogin", isAlreadyLogin)
                    editor.commit()
                    val intent = Intent()
                    intent.setClass(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAuthenticationFail(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(this@Login, errString.toString(), Toast.LENGTH_SHORT).show()
                }
            })
            Toast.makeText(this@Login, "等待解锁", Toast.LENGTH_SHORT).show()
            fingerprintHelper?.startFingerprintUnlock()
        }
    }
    //密码长度大于4
    fun isPwdLegal(pwd:String):Boolean{
        return pwd.length > 4
    }

}