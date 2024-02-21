package com.example.farganaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.databinding.ItemChatBinding
import com.example.farganaapp.model.Chat
const val SENT_BY_NAME = "me"
const val SENT_BY_BOT = "bot"

class MessageAdapter(private val messageList : ArrayList<Chat>) : RecyclerView.Adapter<MessageAdapter.VH>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val message = messageList[position]

        if (message.isFromUser){
            holder.leftChatView.visibility = View.GONE
            holder.rightChatView.visibility = View.VISIBLE
            holder.rightTextView.text = message.prompt
        }else{
            holder.rightChatView.visibility = View.GONE
            holder.leftChatView.visibility = View.VISIBLE
            holder.leftTextView.text = message.prompt
        }
//        if (message.prompt.equals(SENT_BY_NAME)){
//            holder.leftChatView.visibility = View.GONE
//            holder.rightChatView.visibility = View.VISIBLE
//            holder.rightTextView.text = message.prompt
//        }else{
//            holder.rightChatView.visibility = View.GONE
//            holder.leftChatView.visibility = View.VISIBLE
//            holder.leftTextView.text = message.prompt
//        }

    }

    override fun getItemCount() = messageList.size

    class VH(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root){
        val leftChatView = binding.leftChatView
        val leftTextView = binding.leftChatTextView
        val rightChatView = binding.rightChatView
        val rightTextView = binding.rightChatTextView
    }

}