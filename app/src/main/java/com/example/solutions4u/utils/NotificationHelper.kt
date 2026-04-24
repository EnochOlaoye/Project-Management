package com.example.solutions4u.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.solutions4u.R

object NotificationHelper {

    private const val CHANNEL_ID = "bill_reminders"
    private const val CHANNEL_NAME = "Bill Reminders"

    // Creates the notification channel required for Android 8+
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for upcoming bills due tomorrow"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // Sends a notification for a bill due tomorrow
    fun sendBillDueTomorrowNotification(
        context: Context,
        category: String,
        provider: String,
        amount: Double
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Bill Due Tomorrow!")
            .setContentText("$provider ($category) — €${"%.2f".format(amount)} is due tomorrow.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(
                (category + provider).hashCode(),
                notification
            )
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    fun sendPriceDropNotification(
        context: Context,
        provider: String,
        planName: String,
        oldPrice: Double,
        newPrice: Double
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Price Drop Alert!")
            .setContentText("$provider has decreased their $planName plan from €${"%.2f".format(oldPrice)} to €${"%.2f".format(newPrice)}/month!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(
                (provider + planName).hashCode(),
                notification
            )
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}