package com.maqradars.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val currentTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            .format(Calendar.getInstance().time)
        Log.d("NotificationReceiver", "Notifikasi received at $currentTime")
        
        val type = intent.getStringExtra("type") ?: return

        when (type) {
            "reminder" -> {
                Log.d("NotificationReceiver", "Showing reminder")
                NotificationHelper.showReminderNotification(context)
            }
            "tips" -> {
                Log.d("NotificationReceiver", "Showing tips")
                NotificationHelper.showTipsNotification(context)
            }
        }

        // Re-schedule untuk esok hari
        Log.d("NotificationReceiver", "Rescheduling notifications")
        NotificationScheduler.scheduleNotifications(context)
    }
}

