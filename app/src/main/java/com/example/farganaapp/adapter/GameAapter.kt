package com.example.farganaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.databinding.ItemGameBinding
import com.example.farganaapp.model.GameData


class GameAapter(private val onClickListener:(GameData)-> Unit)
    :ListAdapter<GameData,GameAapter.VH>(DiffCallBack){

        class VH(private val binding: ItemGameBinding, private val context: Context)
            :RecyclerView.ViewHolder(binding.root){
            val btnNext = binding.cvItem

                fun onBind(itemGame:GameData){
                    binding.apply {
                        tvName.setText(itemGame.name!!)
                        viewBg.setImageResource(itemGame.icon!!)
                    }

                   // binding.executePendingBindings()
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(ItemGameBinding.inflate(LayoutInflater.from(parent.context),parent,false),parent.context)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val current = getItem(position)
        holder.btnNext.setOnClickListener {
            onClickListener.invoke(current)
        }

        holder.onBind(current)

    }

    companion object{
        val DiffCallBack =object: DiffUtil.ItemCallback<GameData>(){
            override fun areItemsTheSame(oldItem: GameData, newItem: GameData): Boolean {
               return oldItem.name== newItem.name
            }

            override fun areContentsTheSame(oldItem: GameData, newItem: GameData): Boolean {
              return oldItem==newItem
            }

        }
    }
}