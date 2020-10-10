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

    @Query("SELECT * FROM Member")
    fun getAllMember(): List<Member>

    @Query("SELECT * FROM Category")
    fun getAllCategory(): List<Category>

    @Query("SELECT * FROM Subcategory WHERE category LIKE :category")
    fun getAllSubcategory(category : String): List<Subcategory>

    @Query("SELECT * FROM Hidden")
    fun isInitialized(): List<Hidden>

    //查找uid
    @Query("SELECT * FROM Record WHERE uid LIKE :uid ")
    fun findByUid(uid : Int): Record

    //插入
    //有趣的vararg
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg records: Record)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMember(vararg members: Member)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategory(vararg categorys: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSubcategory(vararg subcategorys: Subcategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun initialize(vararg hide: Hidden)


    //删除
    @Delete
    fun delete(record: Record)
}