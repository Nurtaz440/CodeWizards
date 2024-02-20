package com.example.farganaapp.ui.home.admin.ui

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.farganaapp.databinding.FragmentAdminElectricityBinding
import com.example.farganaapp.reciver.AlarmReceiver
import com.example.farganaapp.reciver.channelId
import com.example.farganaapp.reciver.messageExtra
import com.example.farganaapp.reciver.notificationId
import com.example.farganaapp.reciver.titleExtra
import java.text.DateFormat
import java.util.Calendar
import java.util.Date


class AdminElectricityFragment : Fragment() {

    private var _binding: FragmentAdminElectricityBinding? = null
    val binding get() = _binding!!
    var alarmManager : AlarmManager? = null
    var pendingIntent : PendingIntent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminElectricityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        createNotificationChannel()
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
      //  binding.btnScheduleAlarm.setOnClickListener {
//            var time : Long
//            if ((binding.btnScheduleAlarm).isChecked){
//                Toast.makeText(context,"Alarm On",Toast.LENGTH_SHORT).show()
//
//                var calendar = Calendar.getInstance()
//
//                calendar[Calendar.HOUR_OF_DAY] = binding.fragmentCreatealarmTimePicker.currentHour
//                calendar[Calendar.MINUTE] = binding.fragmentCreatealarmTimePicker.currentMinute
//
//                val intent = Intent(context,AlarmReceiver::class.java)
//                pendingIntent = PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_IMMUTABLE)
//                time = calendar.timeInMillis - calendar.timeInMillis % 60000
//                if (System.currentTimeMillis() > time){
//                    time = if (Calendar.AM_PM == 0){
//                        time + 1000*60*60*12
//                    }else{
//                        time + 1000*60*60*24
//                    }
//                }
//                alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP,time,1000,pendingIntent)
//             }else{
//               alarmManager!!.cancel(pendingIntent)
//                Toast.makeText(context,"Alarm OFF",Toast.LENGTH_SHORT).show()
//            }
      //      scheduleNotification()
  //      }

    }

//    private fun scheduleNotification() {
//        val intent = Intent(requireContext().applicationContext,AlarmReceiver::class.java)
//        val title = binding.etTitle.text.toString()
//        val desc = binding.etDesc.text.toString()
//        intent.putExtra(titleExtra,title)
//        intent.putExtra(messageExtra,desc)
//
//        val  pendingIntent = PendingIntent.getBroadcast(
//            requireContext().applicationContext, notificationId,intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val time = getTime()
//
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//        time,pendingIntent)
//        shouAlert(time,title,desc)
//    }

    private fun shouAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormate = android.text.format.DateFormat.getLongDateFormat(requireActivity().applicationContext)
        val timeFormate = android.text.format.DateFormat.getTimeFormat(requireActivity().applicationContext)

        AlertDialog.Builder(context)
            .setTitle("Notification Scheduler")
            .setMessage("Title:" + title +
            "\nMessage:" + message  +
            "\nAt:" + dateFormate.format(date) + " " + timeFormate.format(date))
            .setPositiveButton("Okey"){_,_->}
            .show()
    }


//    private fun getTime(): Long {
//        val minut = binding.fragmentCreatealarmTimePicker.minute
//        val hour = binding.fragmentCreatealarmTimePicker.hour
//        val day = binding.datePicker.dayOfMonth
//        val month = binding.datePicker.month
//        val year = binding.datePicker.year
//
//        val calendar = Calendar.getInstance()
//        calendar.set(year,month,day,hour,minut)
//        return calendar.timeInMillis
//    }

    private fun createNotificationChannel() {
        val name = "Notif channel"
        val desc = "A description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId,name,importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = desc
        val notificationManager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}