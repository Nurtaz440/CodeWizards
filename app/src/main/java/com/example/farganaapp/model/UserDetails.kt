package com.example.farganaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDetails (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String? = null,
    val country: String? = null,
    val region: String? = null,
    val street: String? = null
    )