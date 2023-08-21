package com.example.sem2_prac7

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar
import java.util.logging.Handler
import kotlin.concurrent.thread

class AlarmService:BroadcastReceiver() {

    val calendar = Calendar.getInstance()
    lateinit var  media:MediaPlayer

    override fun onReceive(context: Context?, intent: Intent?) {

       val intent = Intent(context,DestinationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context!!, "com.example.sem2_prac7")
            .setSmallIcon(R.drawable.baseline_call_24)
            .setContentTitle("Reminder")
            .setContentText(calendar[Calendar.HOUR_OF_DAY].toString()+":"+calendar[Calendar.MINUTE])
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(123,builder.build())

         media = MediaPlayer.create(context,R.raw.music)
        media.start()

    }
}