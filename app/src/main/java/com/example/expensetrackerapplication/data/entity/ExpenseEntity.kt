package com.example.expensetrackerapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ExpenseTable",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["UserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["UserId"])]
    )
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    var expenseId:Int =0,

    @ColumnInfo(name = "UserId")
    var userId : Int ,

    @ColumnInfo(name = "ExpenseDate")
    var expenseDate : String,

    @ColumnInfo(name = "ExpenseAmountt")
    var expenseAmt : Float,

    @ColumnInfo(name="ExpenseCategoryId")
    var expenseCategoryId: Int,

    @ColumnInfo(name="ExpenseCategoryName")
    var expenseCategoryName: String?,

    @ColumnInfo(name="PaymentType")
    var paymentType: Int,

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
