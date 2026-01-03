package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.ExpenseRepository
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.launch

class DayWiseReportViewModel(application : Application) : AndroidViewModel(application = application)
{
    val expenseRepository : ExpenseRepository
    init {
         val dao = AppDatabase.getdatabase(application).ExpenseDao()
         expenseRepository= ExpenseRepository(dao)
    }
    var _closeDayWiseReport = MutableLiveData<Boolean>()
    var closeDayWiseReport : LiveData<Boolean> = _closeDayWiseReport

    var _selectedDate = MutableLiveData<String>(Global.fnGetCurrentDate())
    var selectedDate : LiveData<String> = _selectedDate

    var _expenseSummary = MutableLiveData<String>()
    var expenseSummary : LiveData<String> = _expenseSummary

    var _expenseList = MutableLiveData<List<DayWiseReportModel>>()
    var expenseList : LiveData<List<DayWiseReportModel>> = _expenseList


    fun fnCloseDayWiseReport()
    {
        _closeDayWiseReport.value=true
    }

    fun fnGetExpenseDetails(date: String?){
        viewModelScope.launch {
            try
            {
                var expenseAmtSum = 0.0f
                val res = expenseRepository.fnGetExpenseDetailsPerDate(date)
                var list : MutableList<DayWiseReportModel> = mutableListOf()
                var exPaymentType = ""
                if(res.isNotEmpty())
                {
                    res.forEach { ex ->
                        list.add(DayWiseReportModel(
                            expenseId = ex.expenseId,
                            categoryId= ex.expenseCategoryId,
                            catgeoryName= ex.expenseCategoryName,
                            expenseAmt = Global.fnFormatFloatTwoDigits(ex.expenseAmt?.toFloat() ?:0.00f).toString(),
                            expenseRemarks = ex.expenseRemarks,
                            isDelete = if (ex.expenseStatus == Global.EXPENSE_STATUS_DELETED) "DELETED" else "",
                            paymentType = when{
                                ex.paymentType == Global.PAYMENT_TYPE_CASH -> "CASH"
                                ex.paymentType == Global.PAYMENT_TYPE_CARD -> "CARD"
                                ex.paymentType == Global.PAYMENT_TYPE_UPI -> "UPI"
                                ex.paymentType == Global.PAYMENT_TYPE_OTHER -> "OTHERS"
                                else -> {
                                   when{
                                       (ex.expenseAmtInCash ?: 0.0f) > 0.0f && (ex.expenseAmtInCard  ?: 0.0f) > 0.0f && (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "CASH,CARD,UPI"
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f && (ex.expenseAmtInCard  ?: 0.0f) > 0.0f -> "CASH,CARD"
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f && (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "CASH,UPI"
                                       (ex.expenseAmtInCard  ?: 0.0f) > 0.0f && (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "CARD,UPI"
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f -> "CASH"
                                       (ex.expenseAmtInCard  ?: 0.0f) > 0.0f -> "CARD"
                                       (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "UPI"
                                       else -> ""
                                   }
                                }
                            }
                        ))
                        expenseAmtSum=expenseAmtSum+ (ex.expenseAmt?.toFloat() ?:0.0f )
                    }
                    _expenseSummary.value= Global.fnFormatFloatTwoDigits(expenseAmtSum) .toString()

                    Log.i("EXPENSE DETAILS PER DATE","Expense Details Per Date: $list")
                }
                else
                {
                    Log.i("EXPENSE DETAILS PER DATE","Expense Details Per Date: $list")
                }
            }
            catch (e : Exception)
            {
                Log.e("GET EXPENSE DETAILS","Get Expense Detail Per Date: ${e.message}")
            }
        }
    }

    fun fnDeleteExpense(expenseId: Int?)
    {
        viewModelScope.launch {
            try
            {
                var delStatus = expenseRepository.fnDeleteExpense(expenseId)

                if(delStatus)
                    fnGetExpenseDetails(selectedDate.value)
            }
            catch(e : Exception)
            {
                Log.e("DELETE Expense","Delete Expense: ${e.message}")
            }
        }
    }



}