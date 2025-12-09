package com.example.expensetrackerapplication.data.repositary

import com.example.expensetrackerapplication.data.dao.ExpenseDao
import com.example.expensetrackerapplication.data.entity.ExpenseEntity

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
}