package com.example.catch_mentor.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.databinding.ItemSearchBinding
import com.example.catch_mentor.search.SearchAdapter
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.write.WriteInfoS

class MypageFavpostAdapter(private val context: Context) : RecyclerView.Adapter<MypageFavpostAdapter.ViewHolder>(){

    var datas = mutableListOf<WriteInfoS>()

    interface OnItemClickListener{
        fun onItemClick(v: View, pos : Int)
        fun onFavClick(v: View, pos : Int)
        fun updateF()
    }

    var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("searchAdapter 테스트", datas.size.toString())
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener?.onItemClick(it,position)
        }
        holder.binding.searchListFavButton.setOnClickListener{
            listener?.onFavClick(it, position)
        }
        holder.bind(datas[position])
    }

    fun setData(data : MutableList<WriteInfoS>){
        datas = data
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        private val title: TextView = binding.searchListTitle
        private val subTitle: TextView = binding.searchListContent
        private val subject: TextView = binding.searchListSubject
        private val userName: TextView = binding.searchListUserNickname
        private val userUniv: TextView = binding.searchListUserUniv
        private val userImage: ImageView = binding.searchListImage
        private val userFav = binding.searchListFavButton



        fun bind(item: WriteInfoS) {


            title.text = item.title
            subTitle.text = item.subTitle
            subject.text = item.subject
            userName.text = item.userName
            userUniv.text = item.userUniv
            if(item.isFav){
                userFav.toggle()
            }

            ServerProfile().imageDownload(context, item.userID, userImage)
            Log.d("chatList", item.userID)

        }

    }

}