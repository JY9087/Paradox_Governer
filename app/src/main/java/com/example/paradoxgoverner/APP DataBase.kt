package com.example.paradoxgoverner

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//单例设计模式   这里使用静态内部类
@Database(entities = arrayOf(Record::class,Member::class,Category::class,Subcategory::class,
    Hidden::class,Merchant::class,Item::class,userNameAndPwd::class,Account::class)
    , version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        val instance = Instance.Inst
    }

    var init_member = mutableListOf<Member>()

    //静态内部类
    private object Instance {
        val Inst :AppDatabase= Room.databaseBuilder(
            MainActivity.instance(),
            AppDatabase::class.java,
            "UserRecord.db"
        ).allowMainThreadQueries().build()
    }
}