package com.example.expensetrackerapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExpenseTable")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    var expenseId:Int =0,

    @ColumnInfo(name = "ExpenseDate")
    var expenseDate : String,

    @ColumnInfo(name = "ExpenseAmountt")
    var expenseAmt : Float,

    @ColumnInfo(name="ExpenseCategoryId")
    var expenseCategoryId: Int,

    @ColumnInfo(name="ExpenseAmtInCash")
    var expenseAmtInCash: Float,

    @ColumnInfo(name="ExpenseAmtInCard")
    var expenseAmtInCard : Float,

    @ColumnInfo(name="ExpenseAmtInUpi")
    var expenseAmtInUpi : Float,

    @ColumnInfo(name="ExpenseAmtInOthers")
    var expenseAmtInOthers : Float,

    @ColumnInfo(name = "ExpenseRemarks")
    var expenseRemarks:String,

    @ColumnInfo(name = "ExpenseStatus")
    var expenseStatus:Int

)
