package com.example.catch_mentor.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.starRatingUnitData
import com.example.catch_mentor.databinding.ItemMypageStarratingGiveBinding
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.write.WriteInfoS

class MypageStarratingGiveAdapter (private val context: Context,private val mentoring: Mentoring) : RecyclerView.Adapter<MypageStarratingGiveAdapter.ViewHolder>() {


    var datas = mutableListOf<starRatingUnitData>()
    private var mentorName=""

    interface OnItemClickListener {
        fun onRatingChange(pos: Int, r: Int,data:starRatingUnitData)
    }

    var listener: OnItemClickListener? = null


    fun setOnRatingBarChangeListener(listener: OnItemClickListener){
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMypageStarratingGiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("searchAdapter 테스트", datas.size.toString())
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
        holder.bind(datas[position],position)
    }

    fun setData(data: MutableList<starRatingUnitData>) {
        datas = data
        notifyDataSetChanged()
    }

    fun setMentorName(name:String){
        mentorName=name
    }

    inner class ViewHolder(val binding: ItemMypageStarratingGiveBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: starRatingUnitData,pos:Int) {

            binding.itemMypageStarratingMentorName.setText(mentorName)
            binding.itemMypageStarratingGiveMentoringName.setText(mentoring.title)
            binding.itemMypageStarratingGiveMentoringSubject.setText(mentoring.subject)
            binding.itemMypageStarratingGiveMentoringDate.setText(DateUtility.formatDate(item.time!!,"yyyy년 MM월"))
            binding.itemMypageStarratingGiveRatingbar.setRating(item.starRating?.toFloat()?:0f)

            if(item.id!=null)
                binding.itemMypageStarratingGiveRatingbar.apply{
                    setFocusable(false)
                    setIsIndicator(true)
                    setEnabled(true)
                }
            else{
                binding.itemMypageStarratingGiveRatingbar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    listener?.onRatingChange(pos, rating.toInt(),datas[pos])

                }
            }

        }

    }
}