package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.ExpenseRepository
import com.example.expensetrackerapplication.data.repositary.IncomeRepository
import com.example.expensetrackerapplication.model.CategoryChartModel
import com.example.expensetrackerapplication.model.DayWiseExpenseSummaryModel
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.launch

class DashBoardViewModel(application : Application) : AndroidViewModel(application = application)
{

    private var newExpenseRepository : ExpenseRepository

    private var incomeRepository: IncomeRepository


    init{
        val expenseDao = AppDatabase.getdatabase(application).ExpenseDao()
        newExpenseRepository = ExpenseRepository(expenseDao)
        
        val incomeDao = AppDatabase.getdatabase(application).IncomeDao()
        incomeRepository = IncomeRepository(incomeDao)
    }


    var _incomeAmt = MutableLiveData<String?>("0.00")
    var incomeAmt : LiveData<String?> = _incomeAmt

    var _expenseAmt = MutableLiveData<String?>("0.00")
    var expenseAmt : LiveData<String?> = _expenseAmt

    var _balanceAmt = MutableLiveData<String?>("0.00")
    var  balanceAmt : LiveData<String?> = _balanceAmt

    var _categoryChartList = MutableLiveData<List<CategoryChartModel>>()
    var categoryChartList : LiveData<List<CategoryChartModel>> = _categoryChartList

    var _dayWiseExpenseChartList = MutableLiveData<List<DayWiseExpenseSummaryModel>>()
    var dayWiseExpenseChartList : LiveData<List<DayWiseExpenseSummaryModel>> = _dayWiseExpenseChartList

//    var _dayWiseExpenseChartList = MutableLiveData<List<DayWiseExpenseSummaryModel>>()
//    var dayWiseExpenseChartList : LiveData<List<DayWiseExpenseSummaryModel>> = _dayWiseExpenseChartList

    var _daySummary = MutableLiveData<String?>()
    var daySummary : LiveData<String?> = _daySummary

    var _monthlySummary = MutableLiveData<String?>()
    var monthlySummary : LiveData<String?> = _monthlySummary

    var _yearSummary = MutableLiveData<String?>()
    var yearSummary : LiveData<String?> = _yearSummary

    var _clickBtnThisMonth = MutableLiveData<Boolean>()
    val clickBtnThisMonth : LiveData<Boolean> = _clickBtnThisMonth

    var _clickBtnThisYear = MutableLiveData<Boolean>()
    val clickBtnThisYear : LiveData<Boolean> = _clickBtnThisYear

    var _isLoading = MutableLiveData<Boolean>()
    var isLoading : LiveData<Boolean> = _isLoading

    fun fnGetDaySummary(){
        viewModelScope.launch {
            var summaryAmt=newExpenseRepository.fnGetDaySummary(Global.fnGetCurrentDate())
            if(summaryAmt!=0.0f){
                _daySummary.postValue(summaryAmt.toString())
            }
        }
    }

    fun fnGetMonthlySummary(){
        viewModelScope.launch {
            var summaryAmt=newExpenseRepository.fnGetDaySummary(Global.fnGetCurrentMonth())
            if(summaryAmt!=0.0f){
                _monthlySummary.postValue(summaryAmt.toString())
            }
        }
    }

    fun fnGetYearlySummary(){
        viewModelScope.launch {
            var summaryAmt=newExpenseRepository.fnGetDaySummary(Global.fnGetCurrentYear())
            if(summaryAmt!=0.0f){
                _yearSummary.postValue(summaryAmt.toString())
            }
        }
    }

    fun onCLickBtnThisMonth(){
        viewModelScope.launch {
            try{
                _incomeAmt.postValue("0.00")
                _expenseAmt.postValue("0.00")
                _balanceAmt.postValue("0.00")

                _clickBtnThisMonth.postValue(true)
                _clickBtnThisYear.postValue(false)
                var income = incomeRepository.fnGetIncomePerMonth(Global.fnGetCurrentMonth())
                var expense = newExpenseRepository.fnGetMonthSummary(Global.fnGetCurrentMonth())
                var balance = income-expense
                if(income != 0.0f){
                    _incomeAmt.postValue(income.toString())
                }
                if(expense != 0.0f){
                    _expenseAmt.postValue(expense.toString())
                }
                _balanceAmt.postValue(balance.toString())


                var res1= newExpenseRepository.fnGetCateDetailsPerYear(Global.fnGetCurrentMonth(),Global.fnGetCurrentYear())
                var list1 : MutableList<DayWiseExpenseSummaryModel> = mutableListOf()
                if (res1.isNotEmpty()) {
                    res1.forEach { ob ->
                        list1.add(
                            DayWiseExpenseSummaryModel(
                                expenseDate = ob.expenseDate,
                                expenseAmt = ob.expenseAmt,
                            )
                        )
                    }
                    _dayWiseExpenseChartList.postValue(list1)
                }
            }
            catch (e : Exception){
                Log.e("GET INCOME PER MONTH","Get Income Per Month: ${e.message}")
            }
        }
    }

    fun onClickBtnThisYear(){
        viewModelScope.launch {
            try{
                _incomeAmt.postValue("0.00")
                _expenseAmt.postValue("0.00")
                _balanceAmt.postValue("0.00")

                _clickBtnThisYear.postValue(true)
                _clickBtnThisMonth.postValue(false)
                val income = incomeRepository.fnGetIncomePerYear(Global.fnGetCurrentYear())
                val expense = newExpenseRepository.fnGetYearSummary(Global.fnGetCurrentYear())
                val balance = income-expense
                if(income != 0.0f){
                    _incomeAmt.postValue(income.toString())
                }
                if(expense != 0.0f){
                    _expenseAmt.postValue(expense.toString())
                }
                _balanceAmt.postValue(balance.toString())
            }
            catch (e : Exception){
                Log.e("GET INCOME PER YEAR","Get Income Per Year: ${e.message}")
            }
        }
    }

    fun fnGetCateDetailsPerDay(){
        viewModelScope.launch {
            try{
                var res= newExpenseRepository.fnGetCateDetailsPerDay(Global.fnGetCurrentDate())
                var list : MutableList<CategoryChartModel> = mutableListOf()
                if (res.isNotEmpty()){
                    res.forEach { ob ->
                        list.add(
                            CategoryChartModel(
                                userId = ob.userId,
                                categoryId = ob.categoryId,
                                categoryName = ob.categoryName,
                                expenseAmt= ob.expenseAmt
                            )
                        )
                    }
                    _categoryChartList.postValue(list)
                }
            }
            catch (e : Exception){
                Log.e("GET CATEGORY LIST PER DAY","Get Category Details Per Day: ${e.message}")
            }
        }
    }

    fun fnGetCateDetailsPerMonth(){
        viewModelScope.launch {
            try{
                var res= newExpenseRepository.fnGetCateDetailsPerMonth(Global.fnGetCurrentMonth())
                var list : MutableList<CategoryChartModel> = mutableListOf()
                if (res.isNotEmpty()){
                    res.forEach { ob ->
                        list.add(
                            CategoryChartModel(
                                userId = ob.userId,
                                categoryId = ob.categoryId,
                                categoryName = ob.categoryName,
                                expenseAmt= ob.expenseAmt
                            )
                        )
                    }
                    _categoryChartList.postValue(list)
                }

            }
            catch (e : Exception){
                Log.e("GET CATEGORY LIST PER DAY","Get Category Details Per Day: ${e.message}")
            }
        }
    }

    fun fnGetCateDetailsPerYear(){
        viewModelScope.launch {
            try{
                var res= newExpenseRepository.fnGetCateDetailsPerYear(Global.fnGetCurrentDate())
                var list : MutableList<CategoryChartModel> = mutableListOf()
                if (res.isNotEmpty()){
                    res.forEach { ob ->
                        list.add(
                            CategoryChartModel(
                                userId = ob.userId,
                                categoryId = ob.categoryId,
                                categoryName = ob.categoryName,
                                expenseAmt= ob.expenseAmt
                            )
                        )
                    }
                    _categoryChartList.postValue(list)
                }
            }
            catch (e : Exception){
                Log.e("GET CATEGORY LIST PER DAY","Get Category Details Per Day: ${e.message}")
            }
        }
    }
}