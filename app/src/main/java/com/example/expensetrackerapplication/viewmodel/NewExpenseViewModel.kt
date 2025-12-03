package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.`object`.Global
import java.text.SimpleDateFormat
import java.util.Locale

class NewExpenseViewModel(application: Application) : AndroidViewModel(application)
{

    var _selectedDate = MutableLiveData<String?>(fnGetCurrentDate() )
    var selectedDate : LiveData<String?> = _selectedDate

    var _expenseAmt = MutableLiveData<String?>()
    var expenseAmt : LiveData<String?> = _expenseAmt

    var _selectedCategoryId = MutableLiveData<Int>()
    var selectedCategoryId : LiveData<Int> = _selectedCategoryId

    var _selectedCategoryName = MutableLiveData<String?>()
    var selectedCategoryName : LiveData<String?> = _selectedCategoryName

    var _paymentType = MutableLiveData<Int>(0)
    var paymentType : LiveData<Int> = _paymentType

    var _selectedpaymentType = MutableLiveData<Int>(R.id.idCashPayment)
    var selectedpaymentType : LiveData<Int> = _selectedpaymentType


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




    fun fnClearViews(){
        _selectedDate.value=fnGetCurrentDate()
        _expenseAmt.value=""
        _selectedCategoryId.value=0
        _selectedCategoryName.value=""
        _paymentType.value= Global.PAYMENT_TYPE_CASH
        _selectedpaymentType.value= R.id.idCashPayment
        _expenseRemarks.value=""
    }


    fun fnCashPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CASH
        _selectedpaymentType.value= R.id.idCashPayment
        _amtInCash.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
        Log.v("PAYMENT TYPE","Payment Type: CASH")
    }

    fun fnCardPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CARD
        _selectedpaymentType.value= R.id.idCardPayment
        _amtInCard.value =expenseAmt.value?.toFloatOrNull() ?:0.0f
        Log.v("PAYMENT TYPE","Payment Type: CARD")
    }

    fun fnUpiPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_UPI
        _selectedpaymentType.value= R.id.idUpiPayment
        _amtInUpi.value =expenseAmt.value?.toFloatOrNull() ?:0.0f
        Log.v("PAYMENT TYPE","Payment Type: UPI")
    }

    fun fnOtherPayment(){
         _paymentType.value=Global.PAYMENT_TYPE_OTHER
        _selectedpaymentType.value= R.id.idOthersPayment
        _amtInOthers.value =expenseAmt.value?.toFloatOrNull() ?:0.0f
        Log.v("PAYMENT TYPE","Payment Type: OTHER")
    }
    fun fnGetCurrentDate() : String {

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())

        return currentDate
    }
}