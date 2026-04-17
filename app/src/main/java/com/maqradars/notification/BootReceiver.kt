package com.maqradars.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device booted, rescheduling notifications")
            
            // Check jika notifikasi enabled
            val sharedPref = context.getSharedPreferences("maqradars_prefs", Context.MODE_PRIVATE)
            val notificationsEnabled = sharedPref.getBoolean("notifications_enabled", true)
            
            if (notificationsEnabled) {
                NotificationScheduler.scheduleNotifications(context)
            }
        }
    }
}

