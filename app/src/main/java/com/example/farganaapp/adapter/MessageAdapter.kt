package com.example.farganaapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.farganaapp.databinding.ItemChatBinding
import com.example.farganaapp.model.Chat


class MessageAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val messages = mutableListOf<Pair<String, Int>>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMessages(messages: List<Pair<String, Int>>) {
        this.messages.apply {
            clear()
            addAll(messages)
        }
        notifyDataSetChanged()
    }

inner class UserViewHolder(private val binding: ItemChatBinding) :
    ViewHolder(binding.root) {
    fun bind(message: Pair<String, Int>) {
        binding.rightChatView.visibility = View.VISIBLE
        binding.leftChatView.visibility = View.GONE
        binding.rightChatTextView.text = message.first
    }
}

    inner class GeminiViewHolder(private val binding: ItemChatBinding) :
        ViewHolder(binding.root) {
        fun bind(message: Pair<String, Int>) {
            binding.rightChatView.visibility = View.GONE
            binding.leftChatView.visibility = View.VISIBLE
            binding.leftChatTextView.text = message.first
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding =
                    ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(binding)
            }

            VIEW_TYPE_GEMINI -> {
                val binding =
                    ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GeminiViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            VIEW_TYPE_USER -> {
                val userViewHolder = holder as UserViewHolder
                userViewHolder.bind(message)
            }

            VIEW_TYPE_GEMINI -> {
                val geminiViewHolder = holder as GeminiViewHolder
                geminiViewHolder.bind(message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return messages[position].second
    }

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_GEMINI = 2
    }

//    override fun onBindViewHolder(holder: VH, position: Int) {
//        val message = messageList[position]
//
//        if (message.isFromUser){
//            holder.leftChatView.visibility = View.GONE
//            holder.rightChatView.visibility = View.VISIBLE
//            holder.rightTextView.text = message.prompt
//        }else{
//            holder.rightChatView.visibility = View.GONE
//            holder.leftChatView.visibility = View.VISIBLE
//            holder.leftTextView.text = message.prompt
//        }


}