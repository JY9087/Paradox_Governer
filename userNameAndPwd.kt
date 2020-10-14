package com.example.paradoxgoverner

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class userNameAndPwd (
    @PrimaryKey
    val id:Int,
    var userName: String,
    var password: String
)
