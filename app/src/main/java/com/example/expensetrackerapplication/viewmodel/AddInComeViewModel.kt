package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
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

    var _firestoreCloudId = MutableLiveData<String>()
    var firestoreCloudId : LiveData<String> = _firestoreCloudId

    fun onClickCancel(){
        _isLeave.value = true
    }

    fun onClickSubmit(){

        when{
            selectedDate.value.isNullOrBlank() && income.value.isNullOrBlank() -> {
                _insertStatus.value= ResultState.fail("Add Income Failed")
            }
            income.value.isNullOrBlank() -> {
                _insertStatus.value= ResultState.fail("Add Income Failed")
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
                        income = income.value?.toFloatOrNull(),
                        cloudId = firestoreCloudId.value ?:"",
                        isSynced = 0
                    )
                    var result = incomeRepository.fnInsertIncome(income)
                    if(result)
                        _insertStatus.postValue(ResultState.success("successfully Income Was Added"))
                    else
                        _insertStatus.postValue(ResultState.fail("Add Income Failed"))
                }
            }
            catch (e : Exception){
                Log.e("INSERT INCOME STATUS","Insert Income Status: ${e.message}")
            }
        }
    }
}