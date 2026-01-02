package com.example.expensetrackerapplication.`object`

import java.text.SimpleDateFormat
import java.util.Locale

object Global {
    const val PAYMENT_TYPE_CASH=1
    const val PAYMENT_TYPE_CARD=2
    const val PAYMENT_TYPE_UPI=3
    const val PAYMENT_TYPE_SPLIT=4
    const val PAYMENT_TYPE_OTHER=5

    const val EXPENSE_STATUS_ADDED=0

    const val EXPENSE_STATUS_DELETED=1

    const val EXPENSE_STATUS_EDITED=2

    var lUserId =-1
    var lUserName=""

    var lUserPassword=""

    var lUserMobileNo =""

    var lUssrEmail = ""

    fun fnGetCurrentDate() : String {

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())

        return currentDate
    }
}