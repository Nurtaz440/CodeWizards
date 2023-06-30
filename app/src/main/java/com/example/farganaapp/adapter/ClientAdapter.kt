package com.example.farganaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.R
import com.example.farganaapp.databinding.ItemClientBinding
import com.example.farganaapp.model.ClientData

class ClientAdapter(private val onClickListener:(ClientData)-> Unit)
    : ListAdapter<ClientData, ClientAdapter.VH>(DiffCallBack){

    class VH(private val binding: ItemClientBinding, private val context: Context)
        : RecyclerView.ViewHolder(binding.root){
        val btnNext = binding.cvItem

        fun onBind(itemGame: ClientData){
            binding.apply {
                tvName.setText(itemGame.name!!)
                if (itemGame.status == "off" && itemGame.notification == "off" ){
                    tvStatusValue.setTextColor(ContextCompat.getColor(context,R.color.red))
                    tvNotificationValue.setTextColor(ContextCompat.getColor(context,R.color.red))
                }else if (itemGame.status == "on" && itemGame.notification == "off"){
                    tvStatusValue.setTextColor(ContextCompat.getColor(context,R.color.green))
                    tvNotificationValue.setTextColor(ContextCompat.getColor(context,R.color.red))
                }else if (itemGame.status == "off" && itemGame.notification == "on"){
                    tvStatusValue.setTextColor(ContextCompat.getColor(context,R.color.red))
                    tvNotificationValue.setTextColor(ContextCompat.getColor(context,R.color.green))
                }else{
                    tvStatusValue.setTextColor(ContextCompat.getColor(context,R.color.green))
                    tvNotificationValue.setTextColor(ContextCompat.getColor(context,R.color.green))
                }
                tvStatusValue.setText(itemGame.status)
                tvNotificationValue.setText(itemGame.notification)
            }

            // binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(ItemClientBinding.inflate(LayoutInflater.from(parent.context),parent,false),parent.context)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val current = getItem(position)
        holder.btnNext.setOnClickListener {
            onClickListener.invoke(current)
        }

        holder.onBind(current)

    }

    companion object{
        val DiffCallBack =object: DiffUtil.ItemCallback<ClientData>(){
            override fun areItemsTheSame(oldItem: ClientData, newItem: ClientData): Boolean {
                return oldItem.name== newItem.name
            }

            override fun areContentsTheSame(oldItem: ClientData, newItem: ClientData): Boolean {
                return oldItem==newItem
            }

        }
    }
}