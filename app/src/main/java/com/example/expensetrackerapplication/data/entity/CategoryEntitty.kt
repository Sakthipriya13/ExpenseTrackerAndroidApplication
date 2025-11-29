package com.example.expensetrackerapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class CategoryEntitty(
    @PrimaryKey(autoGenerate = true)
    var categoryId : Int,

    @ColumnInfo(name = "CategoryName")
    var categoryName : String?
)
