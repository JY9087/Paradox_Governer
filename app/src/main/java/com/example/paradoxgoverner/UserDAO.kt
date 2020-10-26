package com.example.paradoxgoverner

import androidx.room.*
import java.sql.Date

@Dao
interface UserDAO {


    //查找
    @Query("SELECT * FROM Record WHERE Description LIKE :description " +
            "LIMIT 1")
    fun findByName(description: String): Record

    //根据Category查找Subcategory
    @Query("SELECT * FROM Subcategory WHERE category LIKE :category")
    fun findSubcategoryByCategory(category : String): List<Subcategory>


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


    //根据UID进行查找
    @Query("SELECT * FROM Member WHERE uid LIKE :member ")
    fun findMemberByUid(member : Int): List<Member>

    @Query("SELECT * FROM Category WHERE uid LIKE :category ")
    fun findCategoryByUid(category : Int): List<Category>

    @Query("SELECT * FROM Subcategory WHERE uid LIKE :subcategory")
    fun findSubcategoryByUid(subcategory : Int): List<Subcategory>

    @Query("SELECT * FROM Merchant WHERE uid LIKE :merchant ")
    fun findMerchantByUid(merchant : Int): List<Merchant>

    @Query("SELECT * FROM Item WHERE uid LIKE :item ")
    fun findItemByUid(item : Int): List<Item>


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
    //好像重名函数可以自动重载？
    //也许我会修改这部分代码
    @Delete
    fun delete(record: Record)

    @Delete
    fun deleteMember(member : Member)

    @Delete
    fun deleteCategory(categpry : Category)

    @Delete
    fun deleteSubcategory(subcategory: Subcategory)

    @Delete
    fun deleteMerchant(merchant: Merchant)

    @Delete
    fun deleteItem(item : Item)


    //------------------------------------------------------------------------------

    //按账户的流水查询，以下是降序和升序
    @Query("SELECT * FROM Record WHERE account = (:SelectAccount)ORDER BY date,time DESC")
    fun getWaterBillDesc(SelectAccount : String): List<Record>

    @Query("SELECT * FROM Record WHERE account = (:SelectAccount)ORDER BY date,time ASC")
    fun getWaterBillAsc(SelectAccount : String): List<Record>

    @Query("SELECT * FROM Record WHERE account = (:SelectAccount)AND income=(:SelectIncome)")
    fun getWaterBillIncome(SelectAccount : String,SelectIncome:Boolean):List<Record>

    //给出日期就可以得到包括要求账户的每日流水数据包
    @Query("SELECT * FROM Record WHERE account = (:SelectAccount) And date = (:SelectDate) AND income=(:SelectIncome) ORDER BY time DESC")
    fun getDateWaterBill(SelectAccount : String,SelectDate : Date, SelectIncome:Boolean): List<Record>

    //分类统计成员一级二级选项的数据包
    @Query("SELECT * FROM Record WHERE account = (:SelectAccount) And date >=(:StartDate) and date<=(:EndDate) and member in (:SelectMember)AND category in(:SelectCategory)And subcategory IN (:SelectSubCategory) AND income=(:SelectIncome)")
    fun getSubCategoryBill(SelectAccount : String, StartDate :Date,EndDate :Date, SelectMember : List<String>,SelectCategory:List<String> ,SelectSubCategory : List<String>, SelectIncome:Boolean): List<Record>

}