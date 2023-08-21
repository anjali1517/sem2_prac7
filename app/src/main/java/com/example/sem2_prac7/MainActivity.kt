package com.example.sem2_prac7

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sem2_prac7.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var alarmManager:AlarmManager
    lateinit var timePicker:MaterialTimePicker
    lateinit var pendingIntent: PendingIntent
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {

            btnSelectTime.setOnClickListener {
                selectTime()
            }

            btnSetTime.setOnClickListener {
                setTime()
            }
            btnCancelTime.setOnClickListener {
                cancelTime()
            }
        }
        createNotification()
    }

    private fun selectTime() {

        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setMinute(0)
            .setHour(12)
            .setTitleText("Select Alarm Time")
            .build()

        timePicker.show(supportFragmentManager,"com.example.sem2_prac7")
        timePicker.addOnPositiveButtonClickListener {
            if(timePicker.hour > 12){

                binding.txtTime.text = String.format("%02d",timePicker.hour-12)+ ":" + String.format("%02d",timePicker.minute)+"PM"

            }else{
                String.format("%02d"+timePicker.hour)+ ":" + String.format("%02d"+timePicker.minute)+"AM"
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    private fun cancelTime() {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmService::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarm is canceled",Toast.LENGTH_LONG).show()
    }

    private fun setTime() {

        var calender = Calendar.getInstance()
        calender[Calendar.HOUR_OF_DAY] = timePicker.hour
        calender[Calendar.MINUTE] = timePicker.minute
        calender[Calendar.SECOND] = 0
        calender[Calendar.MILLISECOND] = 0

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmService::class.java)
         pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calender.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Toast.makeText(this,"Alarm set successfully",Toast.LENGTH_LONG).show()
    }

    fun createNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                "com.example.sem2_prac7",
                "channelName",
                NotificationManager.IMPORTANCE_HIGH
            )
            notification.createNotificationChannel(channel)
        }
    }
}