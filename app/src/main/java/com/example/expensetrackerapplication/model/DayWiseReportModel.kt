package com.example.expensetrackerapplication.model

import androidx.annotation.BoolRes

data class DayWiseReportModel(
    var categoryId : Int,
    var catgeoryName : String,
    var expenseAmt : Float,
    var paymentType : Int,
    var expenseRemarks : String,
    var isDelete : Boolean
)
