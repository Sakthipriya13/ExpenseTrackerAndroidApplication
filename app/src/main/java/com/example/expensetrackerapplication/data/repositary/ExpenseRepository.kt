package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.ExpenseDao
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.viewmodel.DayWiseReportViewModel

class ExpenseRepository(val expenseDao: ExpenseDao)
{
    suspend fun fnInsertExpenseDatasToDb(expenseEntity : ExpenseEntity) : Boolean
    {
        var result = expenseDao.fnInsertNewExpense(expenseEntity)

        if(result > 0)
        {
            return true
        }
        else
        {
            return false
        }
    }

    suspend fun fnGetExpenseDetailsPerDate(date : String): List<DayWiseReportModel>{
        try {
            return expenseDao.fnGetExpensePerDate(date)
        }
        catch (e : Exception){
            Log.e("GET EXPENSE DETAILS PER DATE","Get Expense Details Per Date: ${e.message}")
            return listOf()
        }
    }
}