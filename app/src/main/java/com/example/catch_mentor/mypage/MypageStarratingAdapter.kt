package com.example.catch_mentor.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.databinding.ItemMypageStarratingBinding
import com.example.catch_mentor.interactors.MenteeInteractor
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager

data class starRatingData(
    val subject:String,
    val userName:String,
    val userID:String,
    val rating:String,
    val date:String,
    val mentoringName:String
)

class MypageStarratingAdapter(private val context: Context) : RecyclerView.Adapter<MypageStarratingAdapter.ViewHolder>() {

    var datas = mutableListOf<Mentoring>()

    /*
    데이터에 들어가는거
    멘토링 이름, 멘토 이름, 별점, 날짜, 과목, 프로필 사진, 멘토 아이디
     */

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMypageStarratingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("searchAdapter 테스트", datas.size.toString())
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener?.onItemClick(it, position)
        }
        holder.bind(datas[position])
    }

    fun setData(data: MutableList<Mentoring>) {
        datas = data
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ItemMypageStarratingBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Mentoring) {
            if(UserDataManager.isMentor()){
                item.mentee?.get()?.addOnSuccessListener {
                    it?.let{

                        val mentee= MenteeInteractor().parseData(it)
                        binding.itemMypageStarratingName.setText(mentee.name)
                        ServerProfile().imageDownload(itemView.context,mentee.id.toString(),binding.itemMypageStarratingImage)
                    }
                }
            }
            else{
                item.mentor?.get()?.addOnSuccessListener {
                    it?.let{

                        val mentor=MentorInteractor().parseData(it)
                        binding.itemMypageStarratingName.setText(mentor.name)
                        ServerProfile().imageDownload(itemView.context,mentor.id.toString(),binding.itemMypageStarratingImage)
                    }
                }
            }


            binding.itemMypageStarratingSubject.setText(item.subject)


        }

    }
}