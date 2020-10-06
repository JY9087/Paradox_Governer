package com.example.paradoxgoverner

import androidx.room.*

@Dao
interface UserDAO {
    //全选
    @Query("SELECT * FROM Record")
    fun getAll(): List<Record>

    //查找
    @Query("SELECT * FROM Record WHERE Description LIKE :description " +
            "LIMIT 1")
    fun findByName(description: String): Record

    //查找uid
    @Query("SELECT * FROM Record WHERE uid LIKE :uid ")
    fun findByUid(uid : Int): Record

    //插入
    //有趣的vararg
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg records: Record)


    //删除
    @Delete
    fun delete(record: Record)
}