package com.myapplication.view.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.myapplication.R
import com.myapplication.model.Song
import com.myapplication.recyclerAdapter.SongAdapter
import com.myapplication.repository.Repository
import com.myapplication.utils.showToast
import java.io.File


class MainVM : ViewModel() {

    private val songsList: ArrayList<Song> = ArrayList()
    val adapter by lazy { SongAdapter() }
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var song:Song
    private var songPosition = 0


    private val adapterClick = object : SongAdapter.OnItemClick {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View, position: Int, type: String) {
            when (view.id) {
                R.id.clTop -> {
                    song=songsList[position]
                    songPosition = position
                    stopSong()
                    playSong(view, song)
                    showNotification(view, song.displayName)
                    view.context showToast "Playing ${song.displayName}"
                }
            }
        }

    }

    private fun stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(view: View, songName: String?,drawable:Int=R.drawable.ic_pause) {
        val intentNext = Intent("next")
        val intentPlayPause = Intent("playPause")
        val intentPrevious = Intent("previous")
        val pendingIntentNext = PendingIntent.getBroadcast(view.context, 0, intentNext, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentPlayPause = PendingIntent.getBroadcast(view.context, 0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntentPrevious = PendingIntent.getBroadcast(view.context, 0, intentPrevious, PendingIntent.FLAG_IMMUTABLE)


        val contentView = RemoteViews(view.context.packageName, R.layout.custom_notification)
        contentView.setTextViewText(R.id.tvSongName, songName)
        contentView.setImageViewResource(R.id.ivPause,drawable)
        contentView.setOnClickPendingIntent(R.id.ivNext, pendingIntentNext)
        contentView.setOnClickPendingIntent(R.id.ivPause, pendingIntentPlayPause)
        contentView.setOnClickPendingIntent(R.id.ivPrevious, pendingIntentPrevious)

        val channelid = "Song"
        val notificationBuilder = Notification.Builder(view.context, channelid)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomBigContentView(contentView)
            .setOngoing(true)

        val notificationManager = view.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelid, "Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun playSong(view: View, song: Song) {
        val file = File(song.data)
        mediaPlayer = MediaPlayer.create(view.context, Uri.fromFile(file))
        mediaPlayer?.start()
    }

    init {
        adapter.setOnItemClickListener(adapterClick)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playNextSong(view: View) {
        if (songPosition<(songsList.size-1)) {
            stopSong()
            songPosition += 1
            song = songsList[songPosition]
            playSong(view, song)
            showNotification(view, song.displayName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playPreviousSong(view: View) {
        if (songPosition>0){
            stopSong()
            songPosition -= 1
            song = songsList[songPosition]
            playSong(view, song)
            showNotification(view, song.displayName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playPauseSong(view:View) {
        if (mediaPlayer != null) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                showNotification(view,song.displayName,R.drawable.ic_play)
            } else {
                mediaPlayer?.start()
                showNotification(view,song.displayName,R.drawable.ic_pause)
            }
        }
    }

    fun fetchSongList() {
        songsList.addAll(Repository().fetchSongsList())
        adapter.addSong(songsList)
    }

}