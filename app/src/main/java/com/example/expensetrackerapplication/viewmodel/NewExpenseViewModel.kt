package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.data.repositary.ExpenseRepository
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class NewExpenseViewModel(application: Application) : AndroidViewModel(application)
{
    var expenseRepository : ExpenseRepository

    init{
        var expenseDao = AppDatabase.getdatabase(application).ExpenseDao()
        expenseRepository = ExpenseRepository(expenseDao)
    }

    var _selectedDate = MutableLiveData<String?>(fnGetCurrentDate() )
    var selectedDate : LiveData<String?> = _selectedDate

    var _expenseAmt = MutableLiveData<String?>("")
    var expenseAmt : LiveData<String?> = _expenseAmt

    var _selectedCategoryId = MutableLiveData<Int>(-1)
    var selectedCategoryId : LiveData<Int> = _selectedCategoryId

    var _selectedCategoryName = MutableLiveData<String?>("")
    var selectedCategoryName : LiveData<String?> = _selectedCategoryName

    var _paymentType = MutableLiveData<Int>(-1)
    var paymentType : LiveData<Int> = _paymentType

    // Payment type selected in RadioGroup
    val _selectedpaymentType = MutableLiveData<Int?>(-1)   // IMPORTANT: initially null
    val selectedpaymentType: LiveData<Int?> = _selectedpaymentType

    var _expenseRemarks = MutableLiveData<String?>("")
    var expenseRemarks : LiveData<String?> = _expenseRemarks

    var _amtInCash = MutableLiveData<Float>(0.0f)
    var amtInCash : LiveData<Float> = _amtInCash

    var _amtInCard = MutableLiveData<Float>(0.0f)
    var amtInCard : LiveData<Float> = _amtInCard

    var _amtInUpi = MutableLiveData<Float>(0.0f)
    var amtInUpi : LiveData<Float> = _amtInUpi
    var _amtInOthers = MutableLiveData<Float>(0.0f)
    var amtInOthers : LiveData<Float> = _amtInOthers
    var _valueMissingError = MutableLiveData<String?>()
    var valueMissingError : LiveData<String?> = _valueMissingError
    var _insertStatus = MutableLiveData<Boolean>()
    var insertStatus : LiveData<Boolean> = _insertStatus
    var _showSplitDialog = MutableLiveData<Boolean>()
    var showSplitDialog : LiveData<Boolean> = _showSplitDialog

    var _insertFlag = MutableLiveData<Int>(0)
    var insertFlag : LiveData<Int> = _insertFlag

    var _clearAllFields = MutableLiveData<Boolean>()
    var clearAllFields : LiveData<Boolean> = _clearAllFields

    var _bothFieldsEmptyError = MutableLiveData<String?>()
    var bothFieldsEmptyError : LiveData<String?> = _bothFieldsEmptyError

    var _amtFieldsEmptyError = MutableLiveData<String?>()
    var amtFieldsEmptyError : LiveData<String?> = _amtFieldsEmptyError

    var _paymentFieldsEmptyError = MutableLiveData<String?>()
    var paymentFieldsEmptyError : LiveData<String?> = _paymentFieldsEmptyError

    var _cateFieldsEmptyError = MutableLiveData<String?>()
    var cateFieldsEmptyError : LiveData<String?> = _cateFieldsEmptyError

    fun fnClearViews(){

        _clearAllFields.value=true

        _selectedDate.value=fnGetCurrentDate()
        _expenseAmt.value=""
        _selectedCategoryId.value=-1
        _selectedCategoryName.value=""
        _paymentType.value= -1
        _selectedpaymentType.value= null
        _expenseRemarks.value=""

        _amtInUpi.value=0.0f
        _amtInCard.value=0.0f
        _amtInCash.value=0.0f
        _amtInOthers.value=0.0f
    }

    fun fnAddExpenseToDb()
    {
         when{
            expenseAmt.value.isNullOrBlank() &&
            selectedCategoryId.value ==-1 &&
            selectedCategoryName.value.isNullOrBlank() &&
            paymentType.value== -1 &&
            selectedpaymentType.value==-1 -> {
                _valueMissingError.value = "All fields are empty."
            }

            selectedDate.value.isNullOrBlank() -> {
                _valueMissingError.value = "Date Was Missing, So Select Date"
            }

            expenseAmt.value.isNullOrBlank() -> {
                _valueMissingError.value = "Expense Amount Was Missing, So Enter Amount"
            }

            selectedCategoryId.value ==-1 && selectedCategoryName.value.isNullOrBlank() -> {
                 _valueMissingError.value =  "Select Category"
            }
            _paymentType.value== -1 && _selectedpaymentType.value== -1 -> {
                _valueMissingError.value =  "Select Payment Type"
            }

            else -> {
                if(insertFlag.value==0)
                {
                    fnInsertExpense()
                }
            }
        }
    }

    private fun fnInsertExpense() {
        _insertFlag.value=1
        viewModelScope.launch {
            var expenseEntity = ExpenseEntity(
                expenseId =0,
                expenseDate = selectedDate.value ?: "",
                expenseAmt = expenseAmt.value?.toFloatOrNull() ?:0.0f,
                expenseCategoryId = selectedCategoryId.value ?: -1,
                expenseCategoryName  = selectedCategoryName.value ?: "",
                paymentType = paymentType.value ?: Global.PAYMENT_TYPE_CASH,
                expenseAmtInCash = amtInCash.value ?:0.0f,
                expenseAmtInCard = amtInCard.value ?:0.0f,
                expenseAmtInUpi = amtInUpi.value ?:0.0f,
                expenseAmtInOthers = amtInOthers.value ?:0.0f,
                expenseRemarks = expenseRemarks.value ?: "",
                expenseStatus = Global.EXPENSE_STATUS_ADDED
            )

            var result = expenseRepository.fnInsertExpenseDatasToDb(expenseEntity)
            if(result)
            {
                _insertFlag.value = 0
                _insertStatus.value =true
                fnClearViews()
            }
            else
            {
                _insertFlag.value = 0
                _insertStatus.value =false
                fnClearViews()
            }
        }
    }

    // Set default ONLY during the very first fragment open
    fun applyDefaultIfFirstOpen() {
        if (_selectedpaymentType.value == null || _selectedpaymentType.value != R.id.idCashPayment) {  // only once
            _selectedpaymentType.value = R.id.idCashPayment
            _paymentType.value = Global.PAYMENT_TYPE_CASH
        }
    }

    fun fnCashPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_CASH
        _selectedpaymentType.value= R.id.idCashPayment
        _amtInCash.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
        Log.v("PAYMENT TYPE","Payment Type: CASH")
        Log.v("DEFAULT PAYMENT TYPE","Default Payment Type: ${amtInCash.value}")
    }
    fun fnSplitPayment(){
        _paymentType.value=Global.PAYMENT_TYPE_SPLIT
        _selectedpaymentType.value= R.id.idSplitPayment
        _showSplitDialog.value = true
//        _amtInCash.value = expenseAmt.value?.toFloatOrNull() ?:0.0f
        Log.v("PAYMENT TYPE","Payment Type: SPLIT")
//        Log.v("DEFAULT PAYMENT TYPE","Default Payment Type: ${amtInCash.value}")
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