package com.myapplication.recyclerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.databinding.ItemSongLayoutBinding
import com.myapplication.model.Song

class SongAdapter:RecyclerView.Adapter<SongAdapter.SongHolder>() {

    private val list = ArrayList<Song>()
    private var onItemClick: OnItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SongHolder(ItemSongLayoutBinding.inflate(
        LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val data = list[position]
        data.adapterPosition = position
        data.onItemClick = this.onItemClick
        holder.bind(data)
    }

    class SongHolder(private val binding:ItemSongLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(data:Song){
            binding.model = data
        }
    }

    fun setOnItemClickListener(onItemClick: OnItemClick?){
        this.onItemClick = onItemClick
    }

    fun addSong(songList:ArrayList<Song>){
        list.clear()
        list.addAll(songList)
        notifyDataSetChanged()
    }

    interface OnItemClick{
        fun onClick(view: View, position: Int, type:String)
    }

}