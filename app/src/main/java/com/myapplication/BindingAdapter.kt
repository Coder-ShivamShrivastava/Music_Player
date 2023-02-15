package com.myapplication

import androidx.recyclerview.widget.RecyclerView
import com.myapplication.recyclerAdapter.SongAdapter


object BindingAdapter {
    @androidx.databinding.BindingAdapter("setAdapter", requireAll = false)
    @JvmStatic
    fun setRecyclerview(view:RecyclerView?,adapter: SongAdapter){
        view?.adapter = adapter
    }


}