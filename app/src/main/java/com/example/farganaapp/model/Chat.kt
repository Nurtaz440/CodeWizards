package com.example.farganaapp.model

import android.graphics.Bitmap

data class Chat(
    val prompt:String,
    val bitmap: Bitmap?,
    val isFromUser : Boolean
)
