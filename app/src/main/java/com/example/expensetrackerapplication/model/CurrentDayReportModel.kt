package com.example.expensetrackerapplication.model

import androidx.room.ColumnInfo

data class DayWiseReportModel(
    var expenseId : Int?,
    var categoryId : Int?,
    var catgeoryName : String?,
    var expenseAmt : String?,
    var paymentType : String?,
    var expenseRemarks : String?,
    var isDelete : String?
)
