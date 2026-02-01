package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.IncomeEntity

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertIncome(incomeEntity: IncomeEntity) : Long

    @Query("SELECT SUM(Income) From IncomeTable WHERE SUBSTR(date,4,2)= :month")
    suspend fun fnGetIncomePerMonth(month:String) : Float

    @Query("SELECT SUM(Income) From IncomeTable WHERE SUBSTR(date,7,4)= :year")
    suspend fun fnGetIncomePerYear(year:String) : Float
}