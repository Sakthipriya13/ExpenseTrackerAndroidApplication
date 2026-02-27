package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.CategoryEntitty

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertCategories(categoryEntity: CategoryEntitty) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertDefaultCategories(categoryEntity: List<CategoryEntitty>) : List<Long>

    @Query("Select * from Categories WHERE UserId= :lUserId")
    suspend fun fnGetAllCategories(lUserId :Int) : List<CategoryEntitty>

    @Query("Select * from Categories WHERE UserId= :lUserId LIMIT 5")
    suspend fun fnGetDefaultCategories(lUserId :Int) : List<CategoryEntitty>

    @Query("DELETE FROM Categories WHERE categoryId = :categoryId AND UserId= :userId")
    suspend fun fnDeleteCategoryFromDb(categoryId : Int, userId : Int) : Int

}