package com.example.paradoxgoverner

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//单例设计模式   这里使用静态内部类
@Database(entities = arrayOf(userNameAndPwd::class), version = 1,exportSchema = false)
abstract class userNameAndPwdDB : RoomDatabase() {
    abstract fun UserNameAndPwdDAO():userNameAndPwdDAO

    companion object{
        @Volatile
        private var INSTANCE:userNameAndPwdDB?=null
        fun getDatabase(context: Context):userNameAndPwdDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    userNameAndPwdDB::class.java,
                    "DATABSE"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}