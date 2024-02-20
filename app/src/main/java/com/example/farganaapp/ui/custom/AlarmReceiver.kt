package com.example.farganaapp.ui.custom

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.example.farganaapp.MainActivity
import com.example.farganaapp.R

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val event = intent!!.getStringExtra("event")
        val time = intent.getStringExtra("time")

        val notId = intent.getIntExtra("id",0)
        val activityIntent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,activityIntent,PendingIntent.FLAG_IMMUTABLE)

        val channelId = "channelId"
        val name : CharSequence = "channel_name"
        val description = "description"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,name,NotificationManager.IMPORTANCE_HIGH)
            channel.description = description
            val notificationManager = context!!.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notification : Notification = NotificationCompat.Builder(context!!,channelId)
            .setSmallIcon(R.drawable.logo)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(event)
            .setContentText(time)
            .setDeleteIntent(pendingIntent)
            .setGroup("Group_calendar_view")
            .build()

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(notId,notification)
    }
}