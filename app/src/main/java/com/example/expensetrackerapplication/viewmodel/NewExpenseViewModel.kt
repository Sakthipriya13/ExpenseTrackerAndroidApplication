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

    var _amtInCash = MutableLiveData<Float>(0.0f)
    var amtInCash : LiveData<Float> = _amtInCash

    var _amtInCard = MutableLiveData<Float>(0.0f)
    var amtInCard : LiveData<Float> = _amtInCard

    var _amtInUpi = MutableLiveData<Float>(0.0f)
    var amtInUpi : LiveData<Float> = _amtInUpi

    var _amtInOthers = MutableLiveData<Float>(0.0f)
    var amtInOthers : LiveData<Float> = _amtInOthers


    fun fnCashPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CASH
        _amtInCash.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
    }

    fun fnCardPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CARD
        _amtInCard.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
    }

    fun fnUpiPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_UPI
        _amtInUpi.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
    }

//    fun fnSplitPayment(){
//        _paymentType.value=Global.PAYMENT_TYPE_SPLIT
//    }

    fun fnOtherPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_OTHER
        _amtInOthers.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
    }
}