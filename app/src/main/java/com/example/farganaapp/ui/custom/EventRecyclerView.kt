package com.example.farganaapp.ui.custom

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.R
import com.google.android.material.button.MaterialButton

class EventRecyclerView(val context: Context,val arrayList: ArrayList<Events>) : RecyclerView.Adapter<EventRecyclerView.MyViewHolder>() {

    lateinit var dBopenHelper: DBopenHelper

    class MyViewHolder(val item : View) : RecyclerView.ViewHolder(item){
        lateinit var DateText : TextView
        lateinit var EventText : TextView
        lateinit var TimeText : TextView
        lateinit var btnDelete : MaterialButton
        lateinit var setAlarm : ImageView

        init {
            DateText = item.findViewById(R.id.eventDate)
            EventText = item.findViewById(R.id.eventName)
            TimeText = item.findViewById(R.id.eventTime)
            btnDelete = item.findViewById(R.id.btn_delete)
            setAlarm = item.findViewById(R.id.btn_notification)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_events_rowlayout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = arrayList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val events = arrayList[position]

        holder.EventText.setText(events.events)
        holder.DateText.setText(events.date)
        holder.TimeText.setText(events.time)

        holder.btnDelete.setOnClickListener {
            deleteCalendarEvent(events.events,events.date,events.time)
            arrayList.removeAt(position)
            notifyDataSetChanged()
        }
    }
    private fun deleteCalendarEvent(event: String,date : String, time:String){
        dBopenHelper = DBopenHelper(context)
        val database = dBopenHelper.writableDatabase
        dBopenHelper.deleteEvent(event,date,time,database)
        dBopenHelper.close()
    }
}