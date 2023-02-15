package com.myapplication.repository

import android.provider.MediaStore
import com.myapplication.model.Song
import com.myapplication.view.main.MainActivity

class Repository {


    fun fetchSongsList(): ArrayList<Song> {
        val songList = ArrayList<Song>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = (MainActivity.context.get())?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            songList.add(
                Song(
                    id = cursor.getString(0) ?: "",
                    artist = cursor.getString(1) ?: "",
                    title = cursor.getString(2) ?: "",
                    data = cursor.getString(3) ?: "",
                    displayName = cursor.getString(4) ?: "",
                    duration = cursor.getString(5) ?: ""
                )
            )

        }
    return songList
}
}