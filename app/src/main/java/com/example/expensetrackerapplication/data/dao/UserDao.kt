package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertUser(user: UserEntity)

    @Query("SELECT * FROM User WHERE UserName=:name AND UserPassword=:password")
    suspend fun fnCheckUser(name:String?,password:String?): UserEntity
}