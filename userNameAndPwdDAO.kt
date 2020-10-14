package com.example.paradoxgoverner

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface userNameAndPwdDAO {
    @Insert
    fun insertNameAndPwd(NameAndPwd:userNameAndPwd)
    @Query("Select * From userNameAndPwd WHERE :element = userName")
    fun searchNameAndPwd(element:String):Boolean
    @Query("Select * From userNameAndPwd")
    fun findall():List<userNameAndPwd>
    @Query("Select password From userNameAndPwd WHERE :element = userName")
    fun searchPwdByName(element: String):String
}