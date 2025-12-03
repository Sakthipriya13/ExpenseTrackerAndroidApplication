package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.CategoryEntitty

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertCategories(categoryEntitty: CategoryEntitty) : Long

    @Query("Select * from Categories")
    suspend fun fnGetAllCategories() : List<CategoryEntitty>
}