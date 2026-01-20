package com.example.expensetrackerapplication.data.repositary

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
}