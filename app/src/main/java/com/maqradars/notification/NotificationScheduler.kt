package com.maqradars.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object NotificationScheduler {

    private const val NOTIFICATION_REQUEST_CODE_REMINDER = 1001
    private const val NOTIFICATION_REQUEST_CODE_TIPS = 1002
    private const val PREFS_NAME = "maqradars_notification_prefs"
    private const val PREF_HOUR = "notification_hour"
    private const val PREF_MINUTE = "notification_minute"
    private const val DEFAULT_HOUR = 17
    private const val DEFAULT_MINUTE = 10

    fun setNotificationTime(context: Context, hour: Int, minute: Int) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putInt(PREF_HOUR, hour)
            putInt(PREF_MINUTE, minute)
            apply()
        }
        // Re-schedule dengan waktu baru
        scheduleNotifications(context)
    }

    fun getNotificationTime(context: Context): Pair<Int, Int> {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val hour = sharedPref.getInt(PREF_HOUR, DEFAULT_HOUR)
        val minute = sharedPref.getInt(PREF_MINUTE, DEFAULT_MINUTE)
        return Pair(hour, minute)
    }

    fun scheduleNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val (hour, minute) = getNotificationTime(context)

        // Cancel existing alarms first
        cancelNotifications(context)

        // Schedule reminder notification
        scheduleNotification(context, alarmManager, NOTIFICATION_REQUEST_CODE_REMINDER, "reminder", hour, minute)

        // Schedule tips notification (1 detik setelah reminder)
        scheduleNotification(context, alarmManager, NOTIFICATION_REQUEST_CODE_TIPS, "tips", hour, minute, 1000)
    }

    private fun scheduleNotification(
        context: Context,
        alarmManager: AlarmManager,
        requestCode: Int,
        type: String,
        hour: Int,
        minute: Int,
        delayMs: Long = 0
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            // Jika waktu sudah lewat hari ini, jadwalkan besok
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // Add delay jika ada
        if (delayMs > 0) {
            calendar.timeInMillis += delayMs
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = "com.maqradars.NOTIFICATION_ACTION"
            putExtra("type", type)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Coba gunakan setExactAndAllowWhileIdle untuk lebih akurat
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            android.util.Log.d("NotificationScheduler", "Scheduled $type at ${calendar.time}")
        } catch (e: Exception) {
            // Fallback ke setAndAllowWhileIdle jika permission denied
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                android.util.Log.d("NotificationScheduler", "Scheduled $type (fallback) at ${calendar.time}")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun cancelNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val reminderIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "com.maqradars.NOTIFICATION_ACTION"
        }
        val tipsIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "com.maqradars.NOTIFICATION_ACTION"
        }

        val reminderPendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE_REMINDER,
            reminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val tipsPendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE_TIPS,
            tipsIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.cancel(reminderPendingIntent)
            alarmManager.cancel(tipsPendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


