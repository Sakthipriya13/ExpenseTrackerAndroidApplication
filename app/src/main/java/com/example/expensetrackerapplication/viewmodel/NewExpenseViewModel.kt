package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetrackerapplication.`object`.Global

class NewExpenseViewModel(application: Application) : AndroidViewModel(application)
{

    var _selectedDate = MutableLiveData<String?>("")
    var selectedDate : LiveData<String?> = _selectedDate

    var _expenseAmt = MutableLiveData<String?>()
    var expenseAmt : LiveData<String?> = _expenseAmt

    var _selectedCategory = MutableLiveData<Int>()
    var selectedCategory : LiveData<Int> = _selectedCategory

    var _paymentType = MutableLiveData<Int>(0)
    var paymentType : LiveData<Int> = _paymentType

    var _expenseRemarks = MutableLiveData<String?>()
    var expenseRemarks : LiveData<String?> = _expenseRemarks


    fun fnCashPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CASH
    }

    fun fnCardPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CARD
    }

    fun fnUpiPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_UPI
    }

    fun fnSplitPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_SPLIT
    }

    fun fnOtherPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_OTHER
    }
}