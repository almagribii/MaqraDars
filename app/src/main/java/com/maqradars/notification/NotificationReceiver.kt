package com.maqradars.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            // Early null checks
            if (context == null || intent == null) {
                Log.w(TAG, "Context or Intent is null")
                return
            }

            val type = intent.getStringExtra("type")
            if (type.isNullOrEmpty()) {
                Log.w(TAG, "Notification type is missing or empty")
                return
            }

            // Log without heavy date formatting on main thread
            Log.d(TAG, "Notification received - Type: $type at ${System.currentTimeMillis()}")

            when (type) {
                "reminder" -> {
                    Log.d(TAG, "Showing reminder notification")
                    try {
                        NotificationHelper.showReminderNotification(context)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error showing reminder notification", e)
                    }
                }
                "tips" -> {
                    Log.d(TAG, "Showing tips notification")
                    try {
                        NotificationHelper.showTipsNotification(context)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error showing tips notification", e)
                    }
                }
                else -> {
                    Log.w(TAG, "Unknown notification type: $type")
                }
            }

            // Re-schedule notifications for tomorrow
            Log.d(TAG, "Rescheduling notifications")
            try {
                NotificationScheduler.scheduleNotifications(context)
            } catch (e: Exception) {
                Log.e(TAG, "Error scheduling notifications", e)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in NotificationReceiver.onReceive", e)
        }
    }

    companion object {
        private const val TAG = "NotificationReceiver"
    }
}

