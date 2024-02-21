package com.example.farganaapp.ui.home.client.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farganaapp.database.ChatData
import com.example.farganaapp.model.Chat
import com.example.farganaapp.model.ChatState
import com.example.farganaapp.model.ChatUiEvent
import com.example.farganaapp.repository.UserRepository
import com.example.farganaapp.ui.home.client.ProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun onEvent(event:ChatUiEvent){
        when(event){
            is ChatUiEvent.SendPrompt->{
                if (event.prompt.isNotEmpty()){
                    addPrompt(event.prompt,event.bitmap)

                    if (event.prompt != null){
                        getResponseWithImage(event.prompt,event.bitmap)
                    }else{
                        getResponse(event.prompt)
                    }
                }

            }
            is ChatUiEvent.UpdatePrompt->{
                _chatState.update {
                    it.copy(prompt = event.newPrompt)
                }
            }
        }
    }
    private fun addPrompt(prompt:String,bitmap:Bitmap?){
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(prompt,bitmap,true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }
    private fun getResponse(prompt:String){
        viewModelScope.launch {
            val chat = ChatData.getResponse(prompt)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0,chat)
                    }
                )
            }
        }
    }
    private fun getResponseWithImage(prompt:String,bitmap: Bitmap?){
        viewModelScope.launch {
            val chat = ChatData.getResponseWithImage(prompt, bitmap!!)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0,chat)
                    }
                )
            }
        }
    }
    class ChatViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}