package com.example.farganaapp.repository

import androidx.lifecycle.LiveData
import com.example.farganaapp.database.UserDao
import com.example.farganaapp.model.UserDetails

class UserRepository(private val userDao: UserDao) {
    val allUsers: LiveData<UserDetails> = userDao.getAllUsers()
    suspend fun insertUser(user: UserDetails) {
        userDao.insertUser(user)
    }
}