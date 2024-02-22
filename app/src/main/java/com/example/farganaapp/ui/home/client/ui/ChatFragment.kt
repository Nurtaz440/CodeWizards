package com.example.farganaapp.ui.home.client.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farganaapp.R
import com.example.farganaapp.adapter.MessageAdapter
import com.example.farganaapp.database.ChatData
import com.example.farganaapp.databinding.FragmentChatBinding
import com.example.farganaapp.databinding.FragmentClientHomeBinding
import com.example.farganaapp.model.Chat
import com.example.farganaapp.model.ChatUiEvent
import com.example.farganaapp.ui.home.client.ProfileViewModel
import com.example.farganaapp.ui.home.client.ui.viewmodel.ChatViewModel
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    val binding get() = _binding!!
   private lateinit var viewModel: ChatViewModel
//    lateinit var messageList: ArrayList<Chat>
//    private lateinit var messageAdapter: MessageAdapter

   // private val viewModel by viewModels<ChatViewModel>()
    private val chatAdapter = MessageAdapter()
    private val messageList = mutableListOf<Pair<String, Int>>()
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
           val geminiAi  =  GenerativeModel(
            modelName = "gemini-pro",
            apiKey =  ChatData.api_key
        )
        viewModel = ViewModelProvider(
            this,
            ChatViewModel.ChatViewModelFactory(geminiAi))
            .get(ChatViewModel::class.java)
        setAdapter()
        sendMessage()
        observe()
    }
    private fun setAdapter(){
        val ll = LinearLayoutManager(requireContext())
        ll.stackFromEnd = true
        binding.rvMain.layoutManager = ll
        binding.rvMain.setHasFixedSize(true)
        binding.rvMain.adapter = chatAdapter
    }
    private fun sendMessage(){
        binding.ivSend.setOnClickListener {
            val userMessage = binding.evSend.text.toString()
            viewModel.geminiChat(userMessage)
            messageList.add(Pair(userMessage,MessageAdapter.VIEW_TYPE_USER))
            chatAdapter.setMessages(messageList)
            scrollPosition()
            binding.evSend.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    private fun observe(){
        viewModel.messageResponse.observe(viewLifecycleOwner){content ->
            content.text?.let {
                messageList.add(Pair(it,MessageAdapter.VIEW_TYPE_GEMINI))
                chatAdapter.setMessages(messageList)
                scrollPosition()
            }
        }
    }

    private fun scrollPosition(){
        binding.rvMain.smoothScrollToPosition(chatAdapter.itemCount - 1)

    }
//        chatViewModel = ViewModelProvider(
//            this,
//            ChatViewModel.ChatViewModelFactory()
//        ).get(ChatViewModel::class.java)
//        messageList = ArrayList()
//        messageAdapter = MessageAdapter(messageList)
//
//
//        val conversations = chatViewModel.conversations
//        val isGenerating = chatViewModel.isGenerating
//
//
//        val imageBitmaps: ArrayList<Bitmap> = ArrayList()


//        val photoPicker =
//            rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
//                uris.forEach { uri ->
//                    imageBitmaps.add(
//                        MediaStore.Images.Media.getBitmap(
//                            context.contentResolver,
//                            uri
//                        )
//                    )
//
//                }
//            }

//        binding.rvMain.adapter = messageAdapter
//        val ll = LinearLayoutManager(requireContext())
//        ll.stackFromEnd = true
//        binding.rvMain.layoutManager = ll
//
//
//        binding.ivSend.setOnClickListener {
//            if (binding.evSend.text.toString().trim().isNotBlank() ) {
//                lifecycleScope.launch {
//                    chatViewModel.sendText(binding.evSend.text.toString(), imageBitmaps)
//                }
//                imageBitmaps.clear()
//                messageAdapter.addMessage(Chat(binding.evSend.text.toString(),true))
//
//            } else if (binding.evSend.text.toString().trim().isBlank()) {
//                Toast.makeText(
//                    context,
//                    "Please enter a message",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
  //  }
}
