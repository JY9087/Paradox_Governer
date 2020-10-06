package com.example.paradoxgoverner

import androidx.room.*
import java.sql.Date
import java.sql.Time

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

class TimeConverter{
    @TypeConverter
    fun toTime(timeLong : Long) : Time {
        return Time(timeLong);
    }
    @TypeConverter
    fun fromDate(time : Time) : Long{
        return time.time
    }
}

//声明PrimaryKey  autoGenerate = true之后，构造Record时uid输入0，就会自动生成
@Entity
@TypeConverters(DateConverter::class , TimeConverter::class)
data class Record(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var description: String,
    @ColumnInfo var date: Date,
    @ColumnInfo var time : Time,
    @ColumnInfo var member : String,
    @ColumnInfo var class_level_1 : String,
    @ColumnInfo var class_level_2 : String,
    @ColumnInfo var account : String,
    @ColumnInfo var amount : Float,
    @ColumnInfo var category : String
)


@Entity
data class Member(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var member: String
)

@Entity
data class Class_Level_1(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var class_level_1: String
)

@Entity
data class Class_Level_2(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var class_level_1: String,
    @ColumnInfo var class_level_2: String
)

//account表待实现