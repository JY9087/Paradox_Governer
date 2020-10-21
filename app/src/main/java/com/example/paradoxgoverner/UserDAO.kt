package com.example.paradoxgoverner

import androidx.room.*

@Dao
interface UserDAO {


    //查找
    @Query("SELECT * FROM Record WHERE Description LIKE :description " +
            "LIMIT 1")
    fun findByName(description: String): Record

    @Query("SELECT * FROM Record WHERE uid LIKE :uid ")
    fun findByUid(uid : Int): Record

    @Query("SELECT * FROM Record WHERE type LIKE :type ")
    fun findByType(type : String): List<Record>

    @Query("SELECT * FROM Member WHERE member LIKE :member ")
    fun findMemberByString(member : String): List<Member>

    @Query("SELECT * FROM Category WHERE category LIKE :category ")
    fun findCategoryByString(category : String): List<Category>

    @Query("SELECT * FROM Subcategory WHERE subcategory LIKE :subcategory AND category LIKE :category")
    fun findSubcategoryByString(category : String , subcategory : String): List<Subcategory>

    @Query("SELECT * FROM Merchant WHERE merchant LIKE :merchant ")
    fun findMerchantByString(merchant : String): List<Merchant>

    @Query("SELECT * FROM Item WHERE item LIKE :item ")
    fun findItemByString(item : String): List<Item>


    //全选
    @Query("SELECT * FROM Record")
    fun getAll(): List<Record>

    @Query("SELECT * FROM Member")
    fun getAllMember(): List<Member>

    @Query("SELECT * FROM Merchant")
    fun getAllMerchant(): List<Merchant>

    @Query("SELECT * FROM Item")
    fun getAllItem(): List<Item>

    @Query("SELECT * FROM Category")
    fun getAllCategory(): List<Category>

    @Query("SELECT * FROM Subcategory WHERE category LIKE :category")
    fun getAllSubcategory(category : String): List<Subcategory>

    @Query("SELECT * FROM Hidden")
    fun isInitialized(): List<Hidden>



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
    fun insertAllMerchant(vararg merchants: Merchant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllItem(vararg items: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun initialize(vararg hide: Hidden)


    //删除
    @Delete
    fun delete(record: Record)
}