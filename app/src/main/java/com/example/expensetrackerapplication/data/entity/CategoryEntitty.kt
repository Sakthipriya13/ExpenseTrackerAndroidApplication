package com.example.expensetrackerapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Categories",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["UserId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices =[Index(value = ["UserId"])]
    )
data class CategoryEntitty(
    @PrimaryKey(autoGenerate = true)
    var categoryId : Int,

    @ColumnInfo(name = "CategoryName")
    var categoryName : String?,

    @ColumnInfo(name = "UserId")
    var userId : Int,

    @ColumnInfo(name = "SignUpDate")
    var signUpDate : String

)
