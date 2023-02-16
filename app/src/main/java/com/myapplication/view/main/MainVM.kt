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
import com.myapplication.broadcast.MyService
import com.myapplication.model.Song
import com.myapplication.recyclerAdapter.SongAdapter
import com.myapplication.repository.Repository
import com.myapplication.utils.showToast
import java.io.File


class MainVM : ViewModel() {

    private val songsList: ArrayList<Song> = ArrayList()
    val adapter by lazy { SongAdapter() }
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var song: Song
    private var songPosition = 0


    private val adapterClick = object : SongAdapter.OnItemClick {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(view: View, position: Int, type: String) {
            when (view.id) {
                R.id.clTop -> {
                    song = songsList[position]
                    songPosition = position
                    stopSong()
                    playSong(view, song)
                    view.context.startService(
                        Intent(
                            view.context,
                            MyService::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("songName", song.displayName)
                            .putExtra("isPlay", true)
                            .setAction("play")
                    )
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
        if (songPosition < (songsList.size - 1)) {
            stopSong()
            songPosition += 1
            song = songsList[songPosition]
            playSong(view, song)
            view.context.startService(
                Intent(
                    view.context,
                    MyService::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("songName", song.displayName)
                    .putExtra("isPlay", true)
                    .setAction("play")
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playPreviousSong(view: View) {
        if (songPosition > 0) {
            stopSong()
            songPosition -= 1
            song = songsList[songPosition]
            playSong(view, song)
            view.context.startService(
                Intent(
                    view.context,
                    MyService::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("songName", song.displayName)
                    .putExtra("isPlay", true)
                    .setAction("play")
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playPauseSong(view: View) {
        if (mediaPlayer != null) {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                view.context.startService(
                    Intent(
                        view.context,
                        MyService::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("songName", song.displayName)
                        .putExtra("isPlay", false)
                        .setAction("play")
                )
            } else {
                mediaPlayer?.start()

                view.context.startService(
                    Intent(
                        view.context,
                        MyService::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("songName", song.displayName)
                        .putExtra("isPlay", true)
                        .setAction("play")
                )
            }
        }
    }

    fun fetchSongList() {
        songsList.addAll(Repository().fetchSongsList())
        adapter.addSong(songsList)
    }

}