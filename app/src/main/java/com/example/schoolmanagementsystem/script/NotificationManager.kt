package com.example.schoolmanagementsystem.script

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.schoolmanagementsystem.R

class Notification(var context: Context) {

    var channelID: String = "FCM100"
    var channelName: String = "FCMMessage"
    var notifMangager =
        context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationBuilder: NotificationCompat.Builder
    fun FireNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notifMangager.createNotificationChannel(notificationChannel)

        }
        notificationBuilder = NotificationCompat.Builder (context, channelID)

        notificationBuilder.setSmallIcon(R.drawable.offline)
        notificationBuilder.setContentTitle("You're offline")
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setChannelId(channelID)
        notificationBuilder.setContentText("The app need to be online of it to function")
        notifMangager.notify(100, notificationBuilder.build())
    }

}