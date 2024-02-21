package com.example.farganaapp.ui.custom

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.R
import com.google.android.material.button.MaterialButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
        if (isAlarmed(events.date,events.events,events.time)){
            holder.setAlarm.setImageResource(R.drawable.baseline_notifications_active_24)

        }else{
            holder.setAlarm.setImageResource(R.drawable.baseline_notifications_off_24)

        }

        val dateCalendar = Calendar.getInstance()
        dateCalendar.setTime(convertStringToDate(events.date))
        val alarmYear = dateCalendar.get(Calendar.YEAR)
        val alarmMonth = dateCalendar.get(Calendar.MONTH)
        val alarmDay = dateCalendar.get(Calendar.DAY_OF_MONTH)

        val timeCalendar = Calendar.getInstance()
        timeCalendar.setTime(convertStringToTime(events.time))
        val alarmHour = timeCalendar.get(Calendar.HOUR_OF_DAY)
        val alarmMinut = timeCalendar.get(Calendar.MINUTE)

        holder.setAlarm.setOnClickListener {
            if (isAlarmed(events.date,events.events,events.time)){
                holder.setAlarm.setImageResource(R.drawable.baseline_notifications_off_24)
                cancelAlarm(getRequestCode(events.date,events.events,events.time))
                updateEvent(events.date, event = events.events,events.time,"off")
                notifyDataSetChanged()
            }else{
                holder.setAlarm.setImageResource(R.drawable.baseline_notifications_active_24)
                val alarmCalendar = Calendar.getInstance()
                alarmCalendar.set(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinut)
                setAlarm(alarmCalendar,events.events,events.time,getRequestCode(events.date, event = events.events,events.time))
                updateEvent(events.date, event = events.events,events.time,"on")
                notifyDataSetChanged()
            }
        }
    }
    private fun deleteCalendarEvent(event: String,date : String, time:String){
        dBopenHelper = DBopenHelper(context)
        val database = dBopenHelper.writableDatabase
        dBopenHelper.deleteEvent(event,date,time,database)
        dBopenHelper.close()
    }

    private fun isAlarmed(date: String,event: String,time: String):Boolean{
        var alarm = false
        dBopenHelper = DBopenHelper(context)
        val dataBase = dBopenHelper.readableDatabase
        val cursor = dBopenHelper.readIdEvents(date,event,time,dataBase)
        while (cursor.moveToNext()){
          val notify  = cursor.getString(with(cursor) { getColumnIndex(DBStructure.Notify) })
            if (notify.equals("on")){
                alarm = true
            }else{
                alarm = false
            }
        }
        cursor.close()
        dBopenHelper.close()
        return alarm
    }
    private fun setAlarm(calendar: Calendar, event: String, time: String, requestCode:Int){
        val intent = Intent(context.applicationContext,AlarmReceiver::class.java)
        intent.putExtra("event",event)
        intent.putExtra("time",time)
        intent.putExtra("id",requestCode)
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }
    private fun cancelAlarm(requestCode:Int){
        val intent = Intent(context.applicationContext,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    private fun getRequestCode(date: String,event: String,time: String):Int{
        var code = 0
        dBopenHelper = DBopenHelper(context)
        val dataBase = dBopenHelper.readableDatabase
        val cursor = dBopenHelper.readIdEvents(date,event,time,dataBase)
        while (cursor.moveToNext()){
            code  = cursor.getInt(with(cursor) { getColumnIndex(DBStructure.ID) })

        }
        cursor.close()
        dBopenHelper.close()
        return code
    }
    private fun updateEvent(date: String,event: String,time: String,notify:String){
        dBopenHelper = DBopenHelper(context)
        val database = dBopenHelper.writableDatabase
        dBopenHelper.updateEvent(date,event,time,notify,database)
        dBopenHelper.close()
    }

    private fun convertStringToDate(eventDate : String): Date {
        val formate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var date : Date? = null
        try {
            date = formate.parse(eventDate)
        }catch (e : ParseException){
            e.printStackTrace()
        }
        return date!!
    }
    private fun convertStringToTime(eventDate : String): Date {
        val formate = SimpleDateFormat("kk:mm", Locale.ENGLISH)
        var date : Date? = null
        try {
            date = formate.parse(eventDate)
        }catch (e : ParseException){
            e.printStackTrace()
        }
        return date!!
    }
}