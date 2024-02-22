package com.example.farganaapp.database

import android.graphics.Bitmap
import com.example.farganaapp.model.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {
    val api_key = "AIzaSyDFUe8CCmMGTxOSZCHq-VgQtRLyUZfF1Yw"
}