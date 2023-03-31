package com.example.catch_mentor.chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.chatting.ChattingFragment.chatting.userID
import com.example.catch_mentor.databinding.FragmentChattingItemMeBinding
import com.example.catch_mentor.databinding.FragmentChattingItemYouBinding
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager


class ChattingAdapter(private var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var datas : MutableList<Chat> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bindingMe = FragmentChattingItemMeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val bindingYou = FragmentChattingItemYouBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return when (viewType) {
            1 -> {
                ViewHolder1(bindingMe) //내가 보낸 메시지
            }
            else -> {
                ViewHolder2(bindingYou) //상대가 보낸 메시지
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(datas[position].fromMe){
            return 1
        }else return 2
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(datas[position].fromMe) {
            true -> {
                (holder as ViewHolder1).bind(datas[position], position)
            }
            else -> {
                (holder as ViewHolder2).bind(datas[position], position)
            }
        }
    }

    fun setData(data : MutableList<Chat>){
        datas = data
        notifyDataSetChanged()
    }


    inner class ViewHolder1(val binding: FragmentChattingItemMeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Chat, pos : Int){
            binding.mTvMsg.text = item.content
            binding.mTvTime.text = item.time
            if(pos==0){
                ServerProfile().imageDownload(context, UserDataManager.id!!, binding.mIv)
            }else if(datas[pos-1].fromMe){
                binding.mIv.visibility = View.INVISIBLE
            }else{
                ServerProfile().imageDownload(context, UserDataManager.id!!, binding.mIv)
            }
            if(!item.isChecked){
                binding.mTvCheck.visibility = View.VISIBLE
            }
        }

    }

    inner class ViewHolder2(val binding: FragmentChattingItemYouBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Chat, pos : Int){
            binding.yTvMsg.text = item.content
            binding.yTvTime.text = item.time

            if(pos==0){
                ServerProfile().imageDownload(context, userID, binding.yIv)
            }else if(!datas[pos-1].fromMe){
                binding.yIv.visibility = View.INVISIBLE
            }else{
                ServerProfile().imageDownload(context, userID, binding.yIv)
            }
        }
    }

}
