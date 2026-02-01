package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.IncomeDao
import com.example.expensetrackerapplication.data.entity.IncomeEntity

class IncomeRepository(val incomeDao : IncomeDao) {

    suspend fun fnInsertIncome(incomeEntity: IncomeEntity): Boolean{
        return try {
            var result = incomeDao.fnInsertIncome(incomeEntity)
            if(result>0) true else false
        }
        catch (e : Exception){
            false
        }
    }

    suspend fun fnGetIncomePerMonth(month : String): Float{
        return try {
            incomeDao.fnGetIncomePerMonth(month)
        }
        catch (e : Exception){
            Log.e("GET INCOME PER MONTH","Get Income Per Month: ${e.message}")
            0.0f
        }
    }

    suspend fun fnGetIncomePerYear(year : String): Float{
        return try {
            incomeDao.fnGetIncomePerYear(year)
        }
        catch (e : Exception){
            Log.e("GET INCOME PER YEAR","Get Income Per Year: ${e.message}")
            0.0f
        }
    }
}