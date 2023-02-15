package com.myapplication.broadcast

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService:Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals("left",true)){
            Log.e("intentService","Service")
        }
        return START_STICKY
    }
}