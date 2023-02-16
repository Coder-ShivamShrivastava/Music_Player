package com.myapplication.broadcast

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myapplication.R

class MyService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals("playPause")) {
            val localBroadcastManager = LocalBroadcastManager.getInstance(this)
            val localIntent:Intent = Intent("SHIVAM").putExtra("click","playPause")
            localBroadcastManager.sendBroadcast(localIntent)
        } else if (intent?.action.equals("next")) {
            val localBroadcastManager = LocalBroadcastManager.getInstance(this)
            val localIntent:Intent = Intent("SHIVAM").putExtra("click","next")
            localBroadcastManager.sendBroadcast(localIntent)
        } else if (intent?.action.equals("previous")) {
            val localBroadcastManager = LocalBroadcastManager.getInstance(this)
            val localIntent: Intent = Intent("SHIVAM").putExtra("click", "previous")
            localBroadcastManager.sendBroadcast(localIntent)

        } else if (intent?.action.equals("play")) {
            var isPlay = true
            val songName = intent?.extras?.getString("songName")
            if (intent?.extras?.containsKey("isPlay") == true) {
                isPlay = intent.extras?.getBoolean("isPlay") ?: false
            }
            if (isPlay) {
                showNotification(songName)
            } else {
                showNotification(songName, R.drawable.ic_play)
            }
        } else {
            stopSelf()
        }
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(songName: String?, drawable: Int = R.drawable.ic_pause) {
        val intentNext = Intent(
            this,
            MyService::class.java
        )
        intentNext.action = "next"
        val intentPlayPause = Intent(
            this,
            MyService::class.java
        )
        intentPlayPause.action = "playPause"

        val intentPrevious = Intent(
            this,
            MyService::class.java
        )
        intentPrevious.action = "previous"

        val pendingIntentNext =
            PendingIntent.getService(this, 0, intentNext, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentPlayPause =
            PendingIntent.getService(this, 0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentPrevious =
            PendingIntent.getService(this, 0, intentPrevious, PendingIntent.FLAG_IMMUTABLE)


        val contentView = RemoteViews(this.packageName, R.layout.custom_notification)
        contentView.setTextViewText(R.id.tvSongName, songName)
        contentView.setImageViewResource(R.id.ivPause, drawable)
        contentView.setOnClickPendingIntent(R.id.ivNext, pendingIntentNext)
        contentView.setOnClickPendingIntent(R.id.ivPause, pendingIntentPlayPause)
        contentView.setOnClickPendingIntent(R.id.ivPrevious, pendingIntentPrevious)

        val channelid = "Song"
        val notificationBuilder = Notification.Builder(this, channelid)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomBigContentView(contentView)
            .setOngoing(true)

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelid, "Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


}
