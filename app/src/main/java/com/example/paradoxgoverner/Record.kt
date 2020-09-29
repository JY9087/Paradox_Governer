package com.example.paradoxgoverner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo val Description: String?
)
//为什么声明了autoGenerate = true，构造函数依然需要uid:Int?
//@PrimaryKey(autoGenerate = true) val uid: Int,