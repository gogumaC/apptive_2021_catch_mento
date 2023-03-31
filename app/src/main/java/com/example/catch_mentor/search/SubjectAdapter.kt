package com.example.catch_mentor.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.databinding.ItemPostBinding
import com.example.catch_mentor.databinding.ItemSpinnerBinding
import com.example.catch_mentor.post.PostAdapter

class SubjectAdapter : RecyclerView.Adapter<SubjectAdapter.ViewHolder>(){

    var datas = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener{
        fun onItemClick(v: View, pos : Int)
    }

    var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }


    override fun getItemCount(): Int {
        Log.d("PostAdapter 테스트", datas.size.toString())
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        listener?.onItemClick(it,position)
        }
        holder.bind(datas[position])
    }

    fun setData(data : MutableList<String>){
        datas = data
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemSpinnerBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: String) {
            binding.spinnerTxt.text = item
        }

    }
}