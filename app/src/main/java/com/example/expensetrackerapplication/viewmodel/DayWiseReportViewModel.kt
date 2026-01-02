package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
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

    fun fnGetExpenseDetails(date : String){
        viewModelScope.launch {
            try
            {
                val list = expenseRepository.fnGetExpenseDetailsPerDate(date)
                if(list.isNotEmpty())
                {
                    _expenseList.value = list
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


}