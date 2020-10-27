package com.example.paradoxgoverner

import androidx.room.*

@Dao
interface UserDAO {


    //查找Record
    @Query("SELECT * FROM Record WHERE Description LIKE :description " +
            "LIMIT 1")
    fun findRecordByDescription(description: String): Record

    @Query("SELECT * FROM Record WHERE uid LIKE :uid ")
    fun findRecordByUid(uid : Int): Record

    @Query("SELECT * FROM Record WHERE type LIKE :type ")
    fun findRecordByType(type : String): List<Record>

    @Query("SELECT * FROM Record WHERE member LIKE :member ")
    fun findRecordByMember(member : String): List<Record>

    @Query("SELECT * FROM Record WHERE category LIKE :category ")
    fun findRecordByCategory(category : String): List<Record>

    @Query("SELECT * FROM Record WHERE subcategory LIKE :subcategory")
    fun findRecordBySubcategory(subcategory : String): List<Record>

    @Query("SELECT * FROM Record WHERE merchant LIKE :merchant ")
    fun findRecordByMerchant(merchant: String) : List<Record>

    @Query("SELECT * FROM Record WHERE item LIKE :item ")
    fun findRecordByItem(item: String) : List<Record>

    @Query("SELECT * FROM Record WHERE account LIKE :account ")
    fun findRecordByAccount(account: String) : List<Record>


    //其他查找

    //根据Category查找Subcategory
    @Query("SELECT * FROM Subcategory WHERE category LIKE :category")
    fun findSubcategoryByCategory(category : String): List<Subcategory>



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

    @Query("SELECT * FROM Account WHERE account LIKE :account ")
    fun findAccountByString(account : String): List<Account>


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

    @Query("SELECT * FROM Account WHERE uid LIKE :account ")
    fun findAccountByUid(account : Int): List<Account>




    //全选
    @Query("SELECT * FROM Record")
    fun getAllRecord(): List<Record>

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

    @Query("SELECT * FROM Account")
    fun getAllAccount(): List<Account>

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
    fun insertAllAccount(vararg accounts : Account)

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

    @Delete
    fun deleteAccount(account : Account)

    //User Name And Password
    @Insert
    fun insertNameAndPwd(NameAndPwd:userNameAndPwd)
    @Query("Select * From userNameAndPwd WHERE :element = userName")
    fun searchNameAndPwd(element:String):Boolean
    @Query("Select * From userNameAndPwd")
    fun findAllUserNameAndPwd():List<userNameAndPwd>
    @Query("Select password From userNameAndPwd WHERE :element = userName")
    fun searchPwdByName(element: String):String
    

}