package com.example.farganaapp.ui.home.client.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farganaapp.adapter.MessageAdapter
import com.example.farganaapp.database.ChatData
import com.example.farganaapp.databinding.FragmentChatBinding
import com.example.farganaapp.ui.home.client.ui.viewmodel.ChatViewModel
import com.google.ai.client.generativeai.GenerativeModel

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    val binding get() = _binding!!
   private lateinit var viewModel: ChatViewModel

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
         val geminiAiImage  =  GenerativeModel(
            modelName = "gemini-pro-vision",
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
            messageList.add(Pair(userMessage, MessageAdapter.VIEW_TYPE_USER))
            chatAdapter.setMessages(messageList)
            scrollPosition()
            binding.evSend.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            binding.imagePromptProgress.visibility = View.VISIBLE
        }
    }

    private fun observe(){
        viewModel.messageResponse.observe(viewLifecycleOwner){content ->
            content.text?.let {
                messageList.add(Pair(it,MessageAdapter.VIEW_TYPE_GEMINI))
                chatAdapter.setMessages(messageList)
                binding.imagePromptProgress.visibility = View.GONE
                scrollPosition()
            }
        }
    }

    private fun scrollPosition(){
        binding.rvMain.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }
}
