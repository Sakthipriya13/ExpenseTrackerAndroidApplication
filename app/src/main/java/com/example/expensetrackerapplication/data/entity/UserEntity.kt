package com.example.expensetrackerapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    var userId : Int = 0,

    @ColumnInfo(name = "UserName")
    var userName : String?,

    @ColumnInfo(name = "UserMobileNo")
    var userMobileNo : String?,

    @ColumnInfo(name = "UserEmail")
    var userEmail : String?,

    @ColumnInfo(name = "UserPassword")
    var userPassword : String?

)