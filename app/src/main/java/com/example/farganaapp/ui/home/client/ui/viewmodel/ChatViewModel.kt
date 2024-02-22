package com.example.farganaapp.ui.home.client.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.launch

class ChatViewModel(private val geminiPro : GenerativeModel) : ViewModel() {
    private val _messageResponse = MutableLiveData<GenerateContentResponse>()
    val messageResponse : LiveData<GenerateContentResponse> get() = _messageResponse
    private val chat : Chat = geminiPro.startChat()

    fun geminiChat(
        message : String
    ){
        viewModelScope.launch {
            _messageResponse.value = chat.sendMessage(message)
        }
    }

//    private val _chatState = MutableStateFlow(ChatState())
//    val chatState = _chatState.asStateFlow()
//    private val geneminiProModel by lazy {
//        GenerativeModel(
//            modelName = "gemini-pro",
//            apiKey = ChatData.api_key
//        ).apply {
//            startChat()
//        }
//    }
//
//    private val geneminiProVisionModel by lazy {
//        GenerativeModel(
//            modelName = "gemini-pro-vision",
//            apiKey =  ChatData.api_key
//        ).apply {
//            startChat()
//        }
//    }
//
//    val isGenerating = MutableStateFlow<Boolean>(false)
//    val conversations = ArrayList<Triple<String, String, List<Bitmap>?>>()
//    suspend fun sendText(textPrompt: String, images: ArrayList<Bitmap>) {
//
//        isGenerating.value = true
//
//        conversations.add(Triple("sent", textPrompt, images.toList()))
//        conversations.add(Triple("received", "", null))
//
//        val generativeModel = if (images.isNotEmpty()) geneminiProVisionModel else geneminiProModel
//
//        val inputContent = content {
//            images.forEach { imageBitmap ->
//                image(imageBitmap)
//            }
//            text(textPrompt)
//        }
//        viewModelScope.launch {
//            generativeModel.generateContentStream(inputContent)
//                .collect { chunk ->
//                    conversations[conversations.lastIndex] = Triple(
//                        "received",
//                        conversations.last().second + chunk.text,
//                        null
//                    )
//                }
//            isGenerating.value = false
//        }
//    }
//    fun onEvent(event:ChatUiEvent){
//        when(event){
//            is ChatUiEvent.SendPrompt->{
//                if (event.prompt.isNotEmpty()){
//                    addPrompt(event.prompt,event.bitmap)
//
//                    if (event.prompt != null){
//                        getResponseWithImage(event.prompt,event.bitmap)
//                    }else{
//                        getResponse(event.prompt)
//                    }
//                }
//
//            }
//            is ChatUiEvent.UpdatePrompt->{
//                _chatState.update {
//                    it.copy(prompt = event.newPrompt)
//                }
//            }
//        }
//    }
//    private fun addPrompt(prompt:String,bitmap:Bitmap?){
//        _chatState.update {
//            it.copy(
//                chatList = it.chatList.toMutableList().apply {
//                    add(0, Chat(prompt,bitmap,true))
//                },
//                prompt = "",
//                bitmap = null
//            )
//        }
//    }
//    private fun getResponse(prompt:String){
//        viewModelScope.launch {
//            val chat = ChatData.getResponse(prompt)
//            _chatState.update {
//                it.copy(
//                    chatList = it.chatList.toMutableList().apply {
//                        add(0,chat)
//                    }
//                )
//            }
//        }
//    }
//    private fun getResponseWithImage(prompt:String,bitmap: Bitmap?){
//        viewModelScope.launch {
//            val chat = ChatData.getResponseWithImage(prompt, bitmap!!)
//            _chatState.update {
//                it.copy(
//                    chatList = it.chatList.toMutableList().apply {
//                        add(0,chat)
//                    }
//                )
//            }
//        }
//    }
    class ChatViewModelFactory(private val geminiPro : GenerativeModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                return ChatViewModel(geminiPro) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}