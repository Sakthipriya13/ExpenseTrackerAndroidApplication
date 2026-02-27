package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.IncomeEntity
import com.example.expensetrackerapplication.data.repositary.IncomeRepository
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.ui_event.ResultState
import kotlinx.coroutines.launch

class AddInComeViewModel(application : Application) : AndroidViewModel(application)
{
    val incomeRepository : IncomeRepository

    init{
        var incomeDao = AppDatabase.getdatabase(application).IncomeDao()
        incomeRepository = IncomeRepository(incomeDao)
    }
    var _selectedDate = MutableLiveData<String>(Global.fnGetCurrentDate())
    var selectedDate : LiveData<String> = _selectedDate


    var _selectedDateUi = MutableLiveData<String>(Global.fnGetCurrentDateUi())
    var selectedDateUi : LiveData<String> = _selectedDateUi

    var _income = MutableLiveData<String>()
    var income : LiveData<String> = _income

    var _isLeave = MutableLiveData<Boolean>()
    var isLeave : LiveData<Boolean> = _isLeave

    var _insertStatus = MutableLiveData<ResultState>()
    var insertStatus : LiveData<ResultState> = _insertStatus

    fun onClickCancel(){
        _isLeave.value = true
    }

    fun onClickSubmit(){

        when{
            selectedDate.value.isNullOrBlank() && income.value.isNullOrBlank() -> {
                _insertStatus.value= ResultState.fail("Both Fields Were Empty")
            }
            income.value.isNullOrBlank() -> {
                _insertStatus.value= ResultState.fail("Income Field Was Empty")
            }
            else -> {
                fnInsertIncome()
            }
        }
    }

    fun fnInsertIncome(){
        viewModelScope.launch {
            try {
                if(!selectedDate.value.isNullOrBlank() && !income.value.isNullOrBlank()){
                    var income = IncomeEntity(
                        incomeId = 0,
                        userId = Global.lUserId,
                        date = selectedDate.value,
                        income = income.value?.toFloatOrNull()
                    )
                    var result = incomeRepository.fnInsertIncome(income)
                    if(result)
                        _insertStatus.postValue(ResultState.success("Successfully Income Was Added"))
                    else
                        _insertStatus.postValue(ResultState.fail("Add Income Was Failed"))
                }
            }
            catch (e : Exception){

            }
        }
    }
}