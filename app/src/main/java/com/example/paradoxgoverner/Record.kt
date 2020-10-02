package com.example.paradoxgoverner

import androidx.room.*
import java.sql.Date

class DateConverter{
    @TypeConverter
    fun toDate(dateLong : Long) : Date {
        return Date(dateLong);
    }
    @TypeConverter
    fun fromDate(date : Date) : Long{
        return date.time
    }
}

//声明PrimaryKey  autoGenerate = true之后，构造Record时uid输入0，就会自动生成
@Entity
@TypeConverters(DateConverter::class)
data class Record(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var Description: String?,
    @ColumnInfo var date: Date
)

