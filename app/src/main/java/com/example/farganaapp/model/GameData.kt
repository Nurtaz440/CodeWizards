package com.example.farganaapp.model

import androidx.annotation.DrawableRes

data class GameData(
    val id:Int = 0,
    val name:Int? = null,
    @DrawableRes
    val icon:Int? = null,

)
