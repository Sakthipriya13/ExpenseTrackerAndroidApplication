package com.example.expensetrackerapplication.model

import androidx.annotation.BoolRes
import androidx.room.ColumnInfo

data class DayWiseReportModel(
    @ColumnInfo("ExpenseCategoryId")
    var categoryId : Int?,
    @ColumnInfo("ExpenseCategoryName")
    var catgeoryName : String?,
    @ColumnInfo(name = "ExpenseAmountt")
    var expenseAmt : String?,
    @ColumnInfo(name="PaymentType")
    var paymentType : String?,
    @ColumnInfo(name = "ExpenseRemarks")
    var expenseRemarks : String?,
    @ColumnInfo(name = "ExpenseStatus")
    var isDelete : Boolean
)
