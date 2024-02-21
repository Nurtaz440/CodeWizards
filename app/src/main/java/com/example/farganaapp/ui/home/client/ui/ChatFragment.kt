package com.example.farganaapp.ui.home.client.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farganaapp.R
import com.example.farganaapp.adapter.MessageAdapter
import com.example.farganaapp.databinding.FragmentChatBinding
import com.example.farganaapp.databinding.FragmentClientHomeBinding
import com.example.farganaapp.model.Chat
import com.example.farganaapp.model.ChatUiEvent
import com.example.farganaapp.ui.home.client.ProfileViewModel
import com.example.farganaapp.ui.home.client.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    val binding get() = _binding!!
    private lateinit var chatViewModel: ChatViewModel
    lateinit var messageList: ArrayList<Chat>
    private lateinit var messageAdapter: MessageAdapter
    private val uriState = MutableStateFlow("")
    private val imagePicker = registerForActivityResult<PickVisualMediaRequest, Uri>(
        ActivityResultContracts.PickVisualMedia()
    ){
        uri->
        uri?.let {
            uriState.update { uri.toString() }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatViewModel = ViewModelProvider(
            this,
            ChatViewModel.ChatViewModelFactory()
        ).get(ChatViewModel::class.java)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(messageList)
        binding.rvMain.adapter = messageAdapter
        val ll = LinearLayoutManager(requireContext())
        ll.stackFromEnd = true
        binding.rvMain.layoutManager = ll
        //val chatState = chatViewModel.chatState.asLiveData().value
        binding.ivSend.setOnClickListener {
            val str = binding.evSend.text.toString().trim()
           chatViewModel.onEvent(ChatUiEvent.SendPrompt(str,bitmap = null))
            uriState.update { "" }
        }
    }
}