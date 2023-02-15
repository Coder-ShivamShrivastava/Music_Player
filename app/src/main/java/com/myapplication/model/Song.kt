package com.myapplication.model

import com.myapplication.recyclerAdapter.AbstractModel

data class Song(var id:String,
                var artist:String,
                var title:String,
                var data:String,
                var displayName:String,
                var duration:String,
                var viewType:Int=0
                ):AbstractModel()
