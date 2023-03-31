package com.example.catch_mentor.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.databinding.ItemAddressBinding

class SearchAddressAdapter: RecyclerView.Adapter<SearchAddressAdapter.ViewHolder>() {
    var datas = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnDeleteClickListener{
        fun onDeleteClick(v: View, pos : Int)
    }

    var listener : OnDeleteClickListener? = null
    fun setOnItemClickListener(listener : OnDeleteClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemAddressBtn.setOnClickListener {
            listener?.onDeleteClick(it,position)
        }
        holder.bind(datas[position])
    }

    inner class ViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        private val address: TextView = binding.itemAddressTxt

        fun bind(item: String) {
            address.text = item
        }
    } 
}