package com.maqradars.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import com.maqradars.MainActivity
import com.maqradars.R
import kotlin.random.Random

object NotificationHelper {
    const val CHANNEL_ID = "maqradars_notifications"
    const val CHANNEL_NAME = "MaqraDars Notifications"
    const val NOTIFICATION_ID_REMINDER = 1
    const val NOTIFICATION_ID_TIPS = 2

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notifikasi Pengingat dan Tips Maqam"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 250, 250)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showReminderNotification(context: Context) {
        val reminders = listOf(
            "Saatnya belajar Maqam hari ini! 🎵",
            "Jangan lupa melatih tilawah Anda 📖",
            "Waktu untuk memperdalam Maqam Mujawwad 🎤",
            "Mari mendengarkan dan meniru qori terbaik 👨‍🎓",
            "Konsistensi dalam belajar Quran adalah kunci ✨"
        )

        val randomReminder = reminders[Random.nextInt(reminders.size)]

        showNotification(
            context,
            NOTIFICATION_ID_REMINDER,
            "Pengingat Belajar Maqam",
            randomReminder
        )
    }

    fun showTipsNotification(context: Context) {
        val tips = listOf(
            "💡 Tip: Dengarkan qori berkali-kali sebelum meniru",
            "💡 Tip: Fokus pada panjang dan pendeknya nada",
            "💡 Tip: Berlatihlah dengan hati yang khusyuk",
            "💡 Tip: Pelajari makna ayat saat berlatih",
            "💡 Tip: Ulang-ulang adalah ibadah yang mulia",
            "💡 Tip: Jangan terburu-buru, kualitas lebih penting",
            "💡 Tip: Mintalah bimbingan dari guru yang berpengalaman"
        )

        val randomTip = tips[Random.nextInt(tips.size)]

        showNotification(
            context,
            NOTIFICATION_ID_TIPS,
            "Motivasi Harian",
            randomTip
        )
    }

    private fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ) {
        try {
            // Cek permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e("NotificationHelper", "POST_NOTIFICATIONS permission not granted")
                    return
                }
            }

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_maqradars_trans)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setVibrate(longArrayOf(0, 250, 250, 250))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))

            Log.d("NotificationHelper", "Sending notification: $title - $message")

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, builder.build())
            
            Log.d("NotificationHelper", "Notification sent successfully - ID: $notificationId")
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Error showing notification: ${e.message}", e)
        }
    }
}

