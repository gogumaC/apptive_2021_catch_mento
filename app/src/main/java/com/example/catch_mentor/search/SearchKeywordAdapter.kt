package com.example.catch_mentor.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.databinding.SearchSelectKeywordItemBinding

class SearchKeywordAdapter: RecyclerView.Adapter<SearchKeywordAdapter.ViewHolder>() {
    var datas = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchSelectKeywordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener{
        fun onItemClick(v: View, pos : Int)
    }

    var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener?.onItemClick(it,position)
        }
        holder.bind(datas[position])
    }

    inner class ViewHolder(val binding: SearchSelectKeywordItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val keyword: TextView = binding.selectKeywordText

        fun bind(item: String) {
            keyword.text = item
        }
    }
}