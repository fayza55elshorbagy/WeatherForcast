package com.wforcast.weatherforcast.ui.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import com.wforcast.weatherforcast.R


object Notifications
    {
        fun showNotification_withSound(context: Context,alert_event:String,alert_detail :String) {
            lateinit var notificationManager: NotificationManager
            lateinit var notificationChannel: NotificationChannel
            lateinit var builder: Notification.Builder
            val channelId = "i.apps.notifications"
            val description = "Test notification"

            notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, afterNotification::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            // checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_cloudy)
                    .setContentTitle("Alert! : "+alert_event)
                    .setContentText(alert_detail)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_cloudy))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(context)
                    .setContentTitle("Alert! : "+alert_event)
                    .setContentText(alert_detail)
                    .setSmallIcon(R.drawable.ic_cloudy)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_cloudy))
                     .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                     .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())
        }

        fun showNotification_withAlarm(context: Context,alert_event:String,alert_detail :String) {
            lateinit var notificationManager: NotificationManager
            lateinit var notificationChannel: NotificationChannel
            lateinit var builder: Notification.Builder
            val channelId = "i.apps.notifications"
            val description = "Test notification"
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mp: MediaPlayer = MediaPlayer.create(context, R.raw.alarm_beeps)
            mp.start()
            val alarm_sound_uri: Uri = Uri.parse("android.resource://"+context.packageName+ "/raw/alarm_beeps")

            notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, afterNotification::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            // checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_cloudy)
                        .setContentTitle("Alert! : "+alert_event)
                        .setContentText(alert_detail)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_cloudy))
                        .setAutoCancel(true)
                        .setSound(alarm_sound_uri)
                        .setContentIntent(pendingIntent)

            } else {

                builder = Notification.Builder(context)
                        .setContentTitle("Alert! : "+alert_event)
                        .setContentText(alert_detail)
                        .setSmallIcon(R.drawable.ic_cloudy)
                        .setAutoCancel(true)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_cloudy))
                        .setSound(alarm_sound_uri)
                        .setContentIntent(pendingIntent)


            }
            notificationManager.notify(1234, builder.build())
        }


    }

