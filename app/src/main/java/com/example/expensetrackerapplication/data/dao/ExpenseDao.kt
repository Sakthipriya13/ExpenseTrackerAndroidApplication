package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.model.CategoryChartModel
import com.example.expensetrackerapplication.model.DayWiseExpenseSummaryModel
import com.example.expensetrackerapplication.model.DayWiseReportModel

@Dao
interface ExpenseDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun fnInsertNewExpense(expenseEntity: ExpenseEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertNewExpense(expenseEntity:ExpenseEntity) : Long

    @Query("SELECT * FROM ExpenseTable")
    suspend fun fnGetAllExpense(): List<ExpenseEntity>

    @Query("SELECT * FROM ExpenseTable WHERE ExpenseDate= :date AND UserId= :luserId")
    suspend fun fnGetExpensePerDate(date: String?,luserId : Int): List<ExpenseEntity>

    @Query("UPDATE ExpenseTable SET ExpenseStatus = :delExpense WHERE expenseId= :id AND UserId= :luserId")
    suspend fun fnDeleteExpensePerId(id: Int?, delExpense: Int,luserId : Int) : Int

    @Query("SELECT SUM(ExpenseAmountt) FROM ExpenseTable WHERE ExpenseDate= :curDate")
    suspend fun fnGetDaySummary(curDate : String):Float


    @Query("SELECT SUM(ExpenseAmountt) FROM ExpenseTable WHERE SUBSTR(ExpenseDate,4,2)= :curMonth")
    suspend fun fnGetMonthSummary(curMonth : String):Float

    @Query("SELECT SUM(ExpenseAmountt) FROM ExpenseTable WHERE SUBSTR(ExpenseDate,7,4)= :curYear")
    suspend fun fnGetYearSummary(curYear : String):Float

    @Query("SELECT UserId AS userId" +
            ",ExpenseCategoryId AS categoryId" +
            ",ExpenseCategoryName AS categoryName" +
            ",SUM(ExpenseAmountt) As expenseAmt" +
            " FROM ExpenseTable WHERE ExpenseDate= :day " +
            "GROUP BY ExpenseCategoryId")
    suspend fun fnGetCatDetailsPerDay(day : String): List<CategoryChartModel>

    @Query(
        "SELECT UserId AS userId" +
                ",ExpenseCategoryId AS categoryId" +
                ",ExpenseCategoryName AS categoryName" +
                ",SUM(ExpenseAmountt) As expenseAmt" +
                " FROM ExpenseTable WHERE ExpenseDate= :month " +
                "GROUP BY ExpenseCategoryId"
    )
    suspend fun fnGetCatDetailsPerMonth(month : String): List<CategoryChartModel>

    @Query("SELECT UserId AS userId" +
            ",ExpenseCategoryId AS categoryId" +
            ",ExpenseCategoryName AS categoryName" +
            ",SUM(ExpenseAmountt) As expenseAmt" +
            " FROM ExpenseTable WHERE ExpenseDate= :year " +
            "GROUP BY ExpenseCategoryId"
    )
    suspend fun fnGetCatDetailsPerYear(year : String): List<CategoryChartModel>

    @Query("""
        WITH RECURSIVE dates(d) AS (
            -- start date (1st of month)
            SELECT date(:year || '-' || :month || '-01')
        
            UNION ALL
        
            -- add 1 day until last day of that month
            SELECT date(d, '+1 day')
            FROM dates
            WHERE d < date(:year || '-' || :month || '-01', '+1 month', '-1 day')
        )
        SELECT
            strftime('%d-%m-%Y', d) AS expenseDate,
            IFNULL(SUM(e.ExpenseAmountt), 0) AS expenseAmt
        FROM dates
        LEFT JOIN ExpenseTable e
            ON date(
                substr(e.ExpenseDate,7,4) || '-' ||
                substr(e.ExpenseDate,4,2) || '-' ||
                substr(e.ExpenseDate,1,2)
            ) = d
        GROUP BY d
        ORDER BY d
        """)
    suspend fun fnGetMonthWiseExpense(
        month: String,
        year: String
    ): List<DayWiseExpenseSummaryModel>


}