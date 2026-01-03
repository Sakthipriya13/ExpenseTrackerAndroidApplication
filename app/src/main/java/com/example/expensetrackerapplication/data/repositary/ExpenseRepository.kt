package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.ExpenseDao
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.`object`.Global

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

    suspend fun fnGetExpenseDetailsPerDate(date: String?): List<ExpenseEntity>{
        try {
            return expenseDao.fnGetExpensePerDate(date,Global.EXPENSE_STATUS_DELETED)
        }
        catch (e : Exception){
            Log.e("GET EXPENSE DETAILS PER DATE","Get Expense Details Per Date: ${e.message}")
            return listOf()
        }
    }

    suspend fun fnDeleteExpense(expenseId: Int?): Boolean {
        try
        {
            var delStatus = expenseDao.fnDeleteExpensePerId(expenseId,Global.EXPENSE_STATUS_DELETED)
            if(delStatus>0)
                return true
            else
                return false
        }
        catch (e : Exception){
            Log.e("DELETE EXPENSE","Delete Expense: ${e.message}")
            return false
        }
    }
}