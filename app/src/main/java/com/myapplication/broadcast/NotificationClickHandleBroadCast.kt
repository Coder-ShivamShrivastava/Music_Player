package com.myapplication.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NotificationClickHandleBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "next" -> {
                context?.let {
                    val localBroadcastManager = LocalBroadcastManager.getInstance(it)
                    val localIntent:Intent = Intent("SHIVAM").putExtra("click","next")
                    localBroadcastManager.sendBroadcast(localIntent)
                }
            }
            "playPause" -> {
                context?.let {
                    val localBroadcastManager = LocalBroadcastManager.getInstance(it)
                    val localIntent:Intent = Intent("SHIVAM").putExtra("click","playPause")
                    localBroadcastManager.sendBroadcast(localIntent)
                }
            }
            "previous" -> {
                context?.let {
                    val localBroadcastManager = LocalBroadcastManager.getInstance(it)
                    val localIntent:Intent = Intent("SHIVAM").putExtra("click","previous")
                    localBroadcastManager.sendBroadcast(localIntent)
                }
            }
        }
    }
}