package com.example.expensetrackerapplication.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertUser(user: UserEntity) : Long

    @Query("SELECT * FROM User WHERE UserName=:name AND UserPassword=:password")
    suspend fun fnGetUserBasedOnUserName(name:String?,password:String?): List<UserEntity>
}