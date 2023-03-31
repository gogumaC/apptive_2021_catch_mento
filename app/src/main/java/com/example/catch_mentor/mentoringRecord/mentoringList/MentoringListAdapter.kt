package com.example.catch_mentor.mentoringRecord.mentoringList

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.databinding.ItemEmptyMentoringListBinding
import com.example.catch_mentor.databinding.ItemMentoringListUnitBinding
import com.example.catch_mentor.interactors.MenteeInteractor
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.DeviceUtil
import com.example.catch_mentor.utils.DeviceUtil.dpToPx
import com.example.catch_mentor.utils.UserDataManager


class MentoringListAdapter(val heightPx:Int):BaseAdapter<RecyclerView.ViewHolder, Mentoring>() {

    override fun getItemViewType(position: Int): Int {

        if(itemList.isEmpty())return 0
        return 1
    }

    override fun getItemCount(): Int {
        return if(itemList.isEmpty()) 1 else itemList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("MentoringListAdapter",viewType.toString())
        if(viewType==0){
            val view=ItemEmptyMentoringListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            view.root.apply{
                setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightPx))//(heightPx).toInt()-95.dpToPx(context) 2000
            }

            return NoMentoringHolder(view)
        }
        else{
            val view=ItemMentoringListUnitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return MentoringListViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MentorListViewHolder->{
                holder.bind(itemList[position] as Mentor)
            }
            is MenteeListViewHolder->{
                holder.bind(itemList[position] as Mentee)
            }
            is MentoringListViewHolder->{
                holder.bind(itemList[position])
            }
        }
    }

    inner class NoMentoringHolder(binding:ItemEmptyMentoringListBinding):RecyclerView.ViewHolder(binding.root){

    }

    inner class MentoringListViewHolder(val binding: ItemMentoringListUnitBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(mentoring:Mentoring){

            mentoring.schedule?.let{
                val scheduleAdapter=MentoringListScheduleAdapter()
                binding.mentoringListUnitSchedule.apply{
                    scheduleAdapter.refreshData(mentoring.schedule)
                    setAdapter(scheduleAdapter)
                    setLayoutManager(LinearLayoutManager(context))
                }
            }

            mentoring.mentee?.get()
                ?.addOnSuccessListener {
                    binding.mentee=MenteeInteractor().parseData(it)
                }
            mentoring.mentor?.get()
                ?.addOnSuccessListener {
                    //TODO 이부분들은 구조상 뷰모델로 가는게 맞을듯
                    val imgInteractor= ServerProfile()
                    imgInteractor.imageDownload(itemView.context,it.id,binding.mentoringLIstUnitMentorImage)
                    binding.mentor=MentorInteractor().parseData(it)
                }

            binding.mentoring=mentoring
            itemView.setOnClickListener {
                publishItemClick.onNext(mentoring)
            }

        }
    }



    inner class MentorListViewHolder(val binding: ItemMentoringListUnitBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(mentor:Mentor){
            binding.mentee=UserDataManager.userData as Mentee
            binding.mentor=mentor
            itemView.setOnClickListener {
              //  publishItemClick.onNext(mentor)
            }

        }
    }
    inner class MenteeListViewHolder(val binding:ItemMentoringListUnitBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(mentee:Mentee){
            binding.mentor=UserDataManager.userData as Mentor
            binding.mentee=mentee
            itemView.setOnClickListener {
               // publishItemClick.onNext(mentee)
            }
        }
    }
}