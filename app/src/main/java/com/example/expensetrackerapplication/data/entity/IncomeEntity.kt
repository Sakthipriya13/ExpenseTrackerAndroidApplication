package com.example.expensetrackerapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "IncomeTable",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity :: class,
            parentColumns = ["userId"],
            childColumns = ["UserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["UserId"])])
data class IncomeEntity(

    @PrimaryKey(autoGenerate = true)
    var incomeId : Int,

    @ColumnInfo(name = "UserId")
    var userId : Int,

    @ColumnInfo(name="Date")
    var date : String?,

    @ColumnInfo(name="Income")
    var income : Float?
)
