package com.myapplication.view.main

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myapplication.R
import com.myapplication.databinding.ActivityMainBinding
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

    companion object{
       lateinit var context:WeakReference<Context>
    }
    lateinit var binding: ActivityMainBinding
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    lateinit var viewModel: MainVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        context = WeakReference(this)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.viewModel = viewModel
        if (checkPermission(this)) {
            fetchSongsFromDevice()
        }
        getBroadcastListener()
    }

    private fun getBroadcastListener(){
        val listener = object : BroadcastReceiver(){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onReceive(p0: Context?, p1: Intent?) {
                val data = p1?.getStringExtra("click")
                when(data){
                    "next" -> {
                        viewModel.playNextSong(binding.root)
                    }
                    "playPause" -> {
                        viewModel.playPauseSong(binding.root)
                    }
                    "previous" -> {
                        viewModel.playPreviousSong(binding.root)
                    }
                }
            }
        }
        // In Local broadcast we can pass custom IntentFilter and make sure same IntentFilter pass from sendBroadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(listener, IntentFilter("SHIVAM"))
    }


    private fun fetchSongsFromDevice() {
        viewModel.fetchSongList()
    }


    private fun checkPermission(
        context: Context
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat
                        .requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun showDialog(
        msg: String, context: Context?,
        permission: String
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    (this), arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            })
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               viewModel.fetchSongList()
//                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}