package com.myapplication.recyclerAdapter

abstract class AbstractModel{
    var adapterPosition: Int = -1
    var onItemClick: SongAdapter.OnItemClick? = null
}