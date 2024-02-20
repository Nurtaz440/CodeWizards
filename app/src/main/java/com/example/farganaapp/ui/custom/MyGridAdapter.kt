package com.example.farganaapp.ui.custom

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.farganaapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects

class MyGridAdapter(
    context: Context,
    val dates: List<Date>,
    val currentDate: Calendar,
    val events: List<Events>
) : ArrayAdapter<Date>(context, R.layout.single_cell_layout) {
    var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val monthDate = dates[position]
        val dateCalendar = Calendar.getInstance()
        dateCalendar.setTime(monthDate)
        val dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = dateCalendar.get(Calendar.MONTH) + 1
        val displayYear = dateCalendar.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH) + 1
        val currentYear = currentDate.get(Calendar.YEAR)


        val displayMonth2 = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth2 = currentDate.get(Calendar.DAY_OF_MONTH)
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.single_cell_layout, parent, false)
        }
        val dayNumber = view!!.findViewById<TextView>(R.id.calendar_day)
        val eventNumber = view!!.findViewById<TextView>(R.id.events_id)
        val dayCarview = view!!.findViewById<LinearLayout>(R.id.days)
        if (displayMonth == currentMonth && displayYear == currentYear) {
            dayNumber.setTextColor(context.resources.getColor(R.color.black))
        } else {
            dayNumber.setTextColor(context.resources.getColor(R.color.unselected_color))
        }
        val c = Calendar.MONTH
        println("Current time => $c")

        if (currentMonth2 == displayMonth2 && currentMonth == c) {
            dayNumber.setTextColor(context.resources.getColor(R.color.white))
            dayNumber.setBackgroundColor(context.resources.getColor(R.color.blue))
            dayCarview.setBackgroundColor(context.resources.getColor(R.color.blue))
        }
        dayNumber.text = dayNo.toString()

        val eventCalendar = Calendar.getInstance()
        val arrayList = ArrayList<String>()
        for (i in 0 until  events.size step 1){
            eventCalendar.setTime(convertStringToDate(events.get(i).date))
            if (dayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH)+1
                && displayYear == eventCalendar.get(Calendar.YEAR)){
                arrayList.add(events.get(i).events)
                eventNumber.setText("Events")
              //  eventNumber.setText("Events" + arrayList.size)
            }
        }
        return view
    }
    private fun convertStringToDate(eventDate : String):Date{
        val formate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var date : Date? = null
        try {
            date = formate.parse(eventDate)
        }catch (e : ParseException){
            e.printStackTrace()
        }
        return date!!
    }

    override fun getPosition(item: Date?): Int {
        return dates.indexOf(item)
    }

    override fun getItem(position: Int): Date? {
        return dates[position]
    }

    override fun getCount(): Int {
        return dates.size
    }

}