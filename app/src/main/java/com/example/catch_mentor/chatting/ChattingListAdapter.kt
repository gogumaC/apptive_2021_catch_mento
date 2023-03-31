package com.example.catch_mentor.chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.R
import com.example.catch_mentor.databinding.ItemChattingListBinding
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.write.WriteInfoS

class ChattingListAdapter(private val context: Context) : RecyclerView.Adapter<ChattingListAdapter.ViewHolder>() {

    var datas = mutableListOf<ChatList>()

    interface OnItemClickListener{
        fun onItemClick(v: View, pos : Int)
    }

    var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChattingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener?.onItemClick(it,position)
        }
        Log.d("chatList", "완2")
        holder.bind(datas[position])
    }

    fun setData(data : MutableList<ChatList>){
        datas = data
        notifyDataSetChanged()
        Log.d("chatList", "완")

    }


    inner class ViewHolder(val binding: ItemChattingListBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: ChatList) {
            binding.chattingListItemChat.text = item.chat
            if(!item.fromMe && !item.isChecked){
                binding.chattingListItemCheck.visibility = View.VISIBLE
            }
            binding.chattingListItemPost.text = item.post
            binding.chattingListItemTime.text = item.time
            binding.chattingListItemMatch.text = item.match
            when(item.match){
                "매칭 중"->{
                    binding.chattingListItemMatch.setBackgroundResource(R.drawable.style_chatting_btn_ing)
                    binding.chattingListItemMatch.setTextColor(ContextCompat.getColor(context, R.color.match_ing))
                }
                "매칭 완료"->{
                    binding.chattingListItemMatch.setBackgroundResource(R.drawable.style_chatting_btn_com)
                    binding.chattingListItemMatch.setTextColor(ContextCompat.getColor(context, R.color.match_com))
                }
                "매칭 실패"->{
                    binding.chattingListItemMatch.setBackgroundResource(R.drawable.style_chatting_btn_fail)
                    binding.chattingListItemMatch.setTextColor(ContextCompat.getColor(context, R.color.match_fail))
                }
            }
            ServerProfile().imageDownload(context, item.userID, binding.chattingListItemImage)
        }

    }

}