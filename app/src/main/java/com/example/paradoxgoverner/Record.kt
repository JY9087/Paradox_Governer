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
//分为收入或支出
@Entity
@TypeConverters(DateConverter::class , TimeConverter::class)
data class Record(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var description: String,
    @ColumnInfo var date: Date,
    @ColumnInfo var time : Time,
    @ColumnInfo var member : String,
    @ColumnInfo var category : String,
    @ColumnInfo var subcategory : String,
    @ColumnInfo var account : String,
    @ColumnInfo var amount : Float,
    @ColumnInfo var type : String,
    @ColumnInfo var income: Boolean,
    @ColumnInfo var merchant : String,
    @ColumnInfo var item : String
)


@Entity
data class Member(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var member: String
)

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var category: String
)

@Entity
data class Subcategory(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var category: String,
    @ColumnInfo var subcategory: String
)

@Entity
data class Hidden(
    @PrimaryKey(autoGenerate = true) val uid : Int
)

@Entity
data class Merchant(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var merchant: String
)

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var item: String
)

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var account: String
)

@Entity
data class userNameAndPwd (
    @PrimaryKey
    val id:Int,
    var userName: String,
    var password: String
)


@Entity
data class Template(
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var name: String,
    @ColumnInfo var member : String,
    @ColumnInfo var category : String,
    @ColumnInfo var subcategory : String,
    @ColumnInfo var account : String,
    @ColumnInfo var type : String,
    @ColumnInfo var income: Boolean,
    @ColumnInfo var merchant : String,
    @ColumnInfo var item : String
)

@Entity
data class Theme (
    @PrimaryKey(autoGenerate = true) val uid : Int,
    @ColumnInfo var theme: Int
)


//account表待实现