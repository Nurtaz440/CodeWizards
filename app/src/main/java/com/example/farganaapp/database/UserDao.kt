package com.example.farganaapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farganaapp.model.UserDetails

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<UserDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDetails)

}