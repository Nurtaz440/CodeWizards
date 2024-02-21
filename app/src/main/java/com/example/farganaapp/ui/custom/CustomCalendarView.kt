package com.example.farganaapp.ui.custom

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.media.Image
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.farganaapp.R
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CustomCalendarView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val MAX_CALENDAR_DAAYS = 42
    val calendar = Calendar.getInstance(Locale.ENGLISH)

    val dates: MutableList<Date> = mutableListOf()

    val eventsList: MutableList<Events> = mutableListOf()
    lateinit var previousBtn: ImageView
    lateinit var nextBtn: ImageView
    lateinit var CurrentDate: TextView
    lateinit var girdView: GridView
    lateinit var alertDialog : AlertDialog
    lateinit var dBopenHelper: DBopenHelper
    lateinit var myGridAdapter: MyGridAdapter
    var alarmYear : Int? = null
    var alarmMonth : Int? = null
    var alarmDay : Int? = null
    var alarmHour : Int? = null
    var alarmMinute : Int? = null

    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    val yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val eventDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    init {
        InitializeLayout()
        setUpCalendar()

        previousBtn.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            setUpCalendar()
        }
        nextBtn.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            setUpCalendar()
        }
        girdView.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val builder = AlertDialog.Builder(context)
                builder.setCancelable(true)
                val addView = LayoutInflater.from(parent!!.context).inflate(R.layout.add_newevent_layout,null)
                val eventName = addView.findViewById<EditText>(R.id.events_name)
                val EventTime = addView.findViewById<TextView>(R.id.tv_event_time)
                val setTime = addView.findViewById<ImageView>(R.id.setevent_time)
                val addEvent = addView.findViewById<MaterialButton>(R.id.addEvent)
                val checkBox = addView.findViewById<CheckBox>(R.id.alarmMe)
                val dateCalendar = Calendar.getInstance()
                dateCalendar.time = dates[position]
                alarmYear = dateCalendar.get(Calendar.YEAR)
                alarmMonth = dateCalendar.get(Calendar.MONTH)
                alarmDay = dateCalendar.get(Calendar.DAY_OF_MONTH)


                setTime.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val hours = calendar.get(Calendar.HOUR_OF_DAY)
                    val minuts = calendar.get(Calendar.MINUTE)

                    val timePickerDialog = TimePickerDialog(addView.context, androidx.appcompat.R.style.Theme_AppCompat_Dialog,
                        object : TimePickerDialog.OnTimeSetListener{
                            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                                val c = Calendar.getInstance()
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                                c.set(Calendar.MINUTE,minute)
                                c.setTimeZone(TimeZone.getDefault())
                                val hformat = SimpleDateFormat("K:mm a", Locale.ENGLISH)
                                val eventTime = hformat.format(c.time)
                                EventTime.setText(eventTime)
                                alarmHour = c.get(Calendar.HOUR_OF_DAY)
                                alarmMinute = c.get(Calendar.MINUTE)

                            }
                        },hours,minuts,false)
                    timePickerDialog.show()
                }
                val date = eventDateFormat.format(dates[position])
                val month = monthFormat.format(dates[position])
                val year = yearFormat.format(dates[position])

                addEvent.setOnClickListener {
                    if (checkBox.isChecked){
                        saveEvent(eventName.text.toString(),EventTime.text.toString(), date,month,year,"on")
                        setUpCalendar()
                        val calendar = Calendar.getInstance()
                        calendar.set(alarmYear!!,alarmMonth!!,alarmDay!!, alarmHour!!,alarmMinute!!)

                        setAlarm(calendar,eventName.text.toString(),EventTime.text.toString(),
                            getRequestCode(date,eventName.text.toString(),EventTime.text.toString()))
                        alertDialog.dismiss()
                    }else{
                        saveEvent(eventName.text.toString(),EventTime.text.toString(), date,month,year,"off")
                        setUpCalendar()
                        alertDialog.dismiss()
                    }

                }
                builder.setView(addView)
                alertDialog = builder.create()
                alertDialog.show()
            }

        })

        girdView.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {
                val date = eventDateFormat.format(dates[position])

                val builder = AlertDialog.Builder(context)
                builder.setCancelable(true)
                val showView = LayoutInflater.from(parent!!.context).inflate(R.layout.show_event_layout,null)

                val recyclerView = showView.findViewById<RecyclerView>(R.id.eventsRV)
                val layoutManager = LinearLayoutManager(showView.context)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                val eventRecyclerViewAdapter = EventRecyclerView(showView.context,collectEventByDate(date))
                recyclerView.adapter = eventRecyclerViewAdapter
                eventRecyclerViewAdapter.notifyDataSetChanged()

                builder.setView(showView)
                alertDialog = builder.create()
                alertDialog.show()

                alertDialog.setOnCancelListener(object : DialogInterface.OnCancelListener{
                    override fun onCancel(dialog: DialogInterface?) {
                        setUpCalendar()
                    }

                })
                return true
            }

        })
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
    private fun collectEventByDate(date : String) : ArrayList<Events> {
        val arrayList = ArrayList<Events>()
        dBopenHelper = DBopenHelper(context)
        val dataBase = dBopenHelper.readableDatabase
        val cursor = dBopenHelper.readEvents(date,dataBase)
        while (cursor.moveToNext()){
            val event :String = cursor.getString(with(cursor) { getColumnIndex(DBStructure.EVENT) })
            val time = cursor.getString(with(cursor) { getColumnIndex(DBStructure.TIME) })
            val Date = cursor.getString(with(cursor) { getColumnIndex(DBStructure.DATE) })
            val month = cursor.getString(with(cursor) { getColumnIndex(DBStructure.MONTH) })
            val year = cursor.getString(with(cursor) { getColumnIndex(DBStructure.YEAR) })
            val events = Events(event,time,Date,month,year)
            arrayList.add(events)
        }
        cursor.close()
        dBopenHelper.close()
        return arrayList
    }

    fun saveEvent(event:String,time:String,date:String,month:String,year:String,notify:String){
        dBopenHelper = DBopenHelper(context)
        val dataBase = dBopenHelper.writableDatabase
        dBopenHelper.saveEvent(event,time,date,month,year,notify,dataBase)
        dBopenHelper.close()
        Toast.makeText(context,"Event Saved", Toast.LENGTH_SHORT).show()
    }
    fun setUpCalendar() {
        val currentDate = dateFormat.format(calendar.time)
        CurrentDate.setText(currentDate)
        dates.clear()
        val monthCalendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH,1)
        val firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth)
        collectEventsPerMonth(monthFormat.format(calendar.time),yearFormat.format(calendar.time))

        while (dates.size < MAX_CALENDAR_DAAYS){
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)

        }
        myGridAdapter = MyGridAdapter(context,dates,calendar,eventsList)
        girdView.adapter = myGridAdapter
    }

    fun InitializeLayout() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_calendar, this)
        nextBtn = view.findViewById<ImageView>(R.id.nextBtn)
        previousBtn = view.findViewById<ImageView>(R.id.previousBtn)
        CurrentDate = view.findViewById<TextView>(R.id.tv_current)
        girdView = view.findViewById<GridView>(R.id.grid_view)
    }

    private fun collectEventsPerMonth(month: String,year: String){
        eventsList.clear()
        dBopenHelper = DBopenHelper(context)
        val database = dBopenHelper.readableDatabase
        val cursor = dBopenHelper.readEventsMonth(month,year,database)
        while (cursor.moveToNext()){
            val event :String = cursor.getString(with(cursor) { getColumnIndex(DBStructure.EVENT) })
            val time = cursor.getString(with(cursor) { getColumnIndex(DBStructure.TIME) })
            val date = cursor.getString(with(cursor) { getColumnIndex(DBStructure.DATE) })
            val month = cursor.getString(with(cursor) { getColumnIndex(DBStructure.MONTH) })
            val year = cursor.getString(with(cursor) { getColumnIndex(DBStructure.YEAR) })
            val events = Events(event,time,date,month,year)
            eventsList.add(events)
        }
        cursor.close()
        dBopenHelper.close()
    }
    private fun setAlarm(calendar: Calendar,event: String,time: String,requestCode:Int){
        val intent = Intent(context.applicationContext,AlarmReceiver::class.java)
        intent.putExtra("event",event)
        intent.putExtra("time",time)
        intent.putExtra("id",requestCode)
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }
}