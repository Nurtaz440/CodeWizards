package com.example.farganaapp.reciver

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Vibrator
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.farganaapp.R


const val notificationId = 1
const val channelId = "channel"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val vibrator = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(4000)
//        Toast.makeText(context,"Alarm ! wake up wake up", Toast.LENGTH_LONG).show()
//
//        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//
//        if (alarmUri == null){
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        }
//        val ringtone = RingtoneManager.getRingtone(context,alarmUri)
//        ringtone.play()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(intent!!.getStringExtra(titleExtra))
            .setContentText(intent!!.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId,notification)
    }
}