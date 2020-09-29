package com.example.paradoxgoverner

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//单例设计模式   这里使用静态内部类
//单例出问题了   没能initialize
@Database(entities = arrayOf(Record::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        val instance = Instance.Inst
    }

    //静态内部类
    private object Instance {
        val Inst :AppDatabase= Room.databaseBuilder(

            //这静态内部类有问题。在Build时使用了MainActivity.instance，所以只能在MainActivity里使用
            //也许可以在其他地方使用？
            MainActivity.instance(),
            AppDatabase::class.java,
            "UserRecord.db"
        ).allowMainThreadQueries().build()
    }
}