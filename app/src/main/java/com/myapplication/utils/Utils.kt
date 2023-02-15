package com.myapplication.utils

import android.content.Context
import android.widget.Toast

infix fun Context.showToast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}