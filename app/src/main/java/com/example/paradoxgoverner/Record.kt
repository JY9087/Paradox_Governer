package com.example.paradoxgoverner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

//声明PrimaryKey  autoGenerate = true之后，构造Record时uid输入0，就会自动生成
@Entity
data class Record(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var Description: String?
) {

}
