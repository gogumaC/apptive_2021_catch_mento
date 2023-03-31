package com.example.catch_mentor.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.dataClass.LearningRecord

import com.example.catch_mentor.databinding.ItemProgressCardBinding
import com.example.catch_mentor.interactors.*
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.example.catch_mentor.utils.DateUtility.getFirstDateOfMonth
import com.example.catch_mentor.utils.DateUtility.getLastDateOfMonth
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class ProgressCardAdapter:BaseAdapter<ProgressCardAdapter.ProgressCardViewHolder, LearningRecord>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressCardViewHolder {
        val binding=ItemProgressCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProgressCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgressCardViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ProgressCardViewHolder(val binding:ItemProgressCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(record:LearningRecord){

            binding.record=record
            record.mentor?.get()?.addOnSuccessListener {
                val mentor=MentorInteractor().parseData(it)

                ServerProfile().imageDownload(itemView.context,mentor.id.toString(),binding.progressCardMentorImg)
                binding.progressCardMentorName.setText(mentor.name)
            }

            record.mentoring?.get()?.addOnSuccessListener {
                val mentoring=MentoringInteractor().parseData(it)
                val taskInteractor=TaskInteractor2(mentoring.id.toString())
                val learningRecordInteractor=LearningRecordInteractor2(mentoring)
                val progressInteractor=ProgressInteractor(mentoring.id.toString())

                if(record.date!=null) {

                        taskInteractor.ref
                            .whereEqualTo("startDate", record.date)
                            .get()
                            .addOnSuccessListener {result->
                                if(result.isEmpty) binding.progressCardTaskDot.setVisibility(View.INVISIBLE)
                                else{
                                    for (document in result) {
                                        Log.d("checkforrr",document.toString())
                                        val task=taskInteractor.parseData(document)
                                        binding.progressCardTaskDot.setVisibility(View.VISIBLE)
                                        binding.progressCardTaskTitle.setText(task.title)
                                        binding.progressCardTaskContent.setText(task.content)

                                    }
                                }

                            }

                    progressInteractor.ref.document(DateUtility.formatDate(record.date,"yyyyMM")).get().addOnSuccessListener {
                        var progress=progressInteractor.parseData(it)
                        if(!it.exists()){
                            progress=mentoring.getMonthProgress(record.date,listOf())
                        }
                        val percentage=progress.getPercentage()
                        Log.d("progressPercent","$progress + $percentage")
                        binding.progressBar.setProgress(percentage)
                        binding.itemProgressCardProgressRateTextView.setText(percentage.toString()+"%")
                        binding.progressCardDone.setText(progress.done.toString())
                        binding.progressCardNotYet.setText(progress.notYet.toString())
                        binding.progressCardProceeding.setText(progress.proceeding.toString())
                    }


//                    calcProgress(record.date,learningRecordInteractor).addOnSuccessListener {result->
//                        val list= mutableListOf<LearningRecord>()
//                        for (document in result) {
//                            Log.d("checkfor",document.toString())
//                            val item=learningRecordInteractor.parseData(document)
//                            list.add(item)
//                        }
//                        val progress=mentoring.getMonthProgress(record.date,list)
//                        val percentage=progress.getPercentage()
//                        Log.d("progressPercent","$progress + $percentage")
//                        binding.progressBar.setProgress(percentage)
//                        binding.itemProgressCardProgressRateTextView.setText(percentage.toString()+"%")
//                        binding.progressCardDone.setText(progress.done.toString())
//                        binding.progressCardNotYet.setText(progress.notYet.toString())
//                        binding.progressCardProceeding.setText(progress.proceeding.toString())
//
//
//                    }




                }

            }


        }
        fun calcProgress(date: Date,interactor:LearningRecordInteractor2):  Task<QuerySnapshot> {
            val firstDay=date.getFirstDateOfMonth()
            val lastDay=firstDay.changeDate(Calendar.DATE,-1)
            return interactor.ref
                .whereGreaterThanOrEqualTo("date",firstDay)
                .whereLessThan("date",lastDay)
                .get()

        }
    }
}