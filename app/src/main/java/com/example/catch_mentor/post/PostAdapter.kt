package com.example.catch_mentor.post

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.databinding.ItemPostBinding


class PostAdapter() : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    var datas = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("PostAdapter 테스트", datas.size.toString())
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    fun setData(data : MutableList<String>){
        datas = data
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: String) {
            binding.itemPost.text = item
        }

    }
}