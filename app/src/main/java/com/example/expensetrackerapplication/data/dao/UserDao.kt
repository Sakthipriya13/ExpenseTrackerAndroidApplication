package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertUser(user: UserEntity) : Long

    @Query("SELECT * FROM User WHERE UserName=:name AND UserPassword=:password")
    suspend fun fnGetUserBasedOnUserName(name:String?,password:String?): List<UserEntity>

    @Query("DELETE FROM User WHERE UserId= :id")
    suspend fun fnDeleteUserAccountFromDb(id: Int) : Int

    @Query("UPDATE USER SET UserPassword= :newPassword WHERE UserId= :userId AND UserPassword= :currentPassword")
    suspend fun fnUpdateUserPassword(newPassword: String?, userId: Int, currentPassword: String?) : Int
}