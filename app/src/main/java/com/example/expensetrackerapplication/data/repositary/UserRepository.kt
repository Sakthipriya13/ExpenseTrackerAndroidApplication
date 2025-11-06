package com.example.expensetrackerapplication.data.repositary

import com.example.expensetrackerapplication.data.dao.UserDao
import com.example.expensetrackerapplication.data.entity.UserEntity

class UserRepository(var userDao: UserDao)
{
    suspend fun fnInsertUserDetails(user: UserEntity)
    {
        userDao.fnInsertUser(user)
    }
}