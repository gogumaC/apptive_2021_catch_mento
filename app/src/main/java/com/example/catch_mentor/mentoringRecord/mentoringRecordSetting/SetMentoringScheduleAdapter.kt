package com.example.catch_mentor.mentoringRecord.mentoringRecordSetting

import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.dataClass.MentoringSchedule
import com.example.catch_mentor.databinding.ItemSetMentoringScheduleBinding
import com.example.catch_mentor.utils.DateUtility
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

class SetMentoringScheduleAdapter:BaseAdapter<SetMentoringScheduleAdapter.SetDetailMentoringScheduleViewHolder, MentoringSchedule>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SetDetailMentoringScheduleViewHolder {
        val binding=ItemSetMentoringScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SetDetailMentoringScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SetDetailMentoringScheduleViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


    fun addMentoringSchedule(dayOfWeek: DayOfWeek){
        itemList.add(MentoringSchedule(dayOfWeek))
        notifyDataSetChanged()
    }



    inner class SetDetailMentoringScheduleViewHolder(val binding:ItemSetMentoringScheduleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(schedule: MentoringSchedule){
            binding.itemSetMentoringScheduleDayOfWeek.setText(schedule.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.KOREAN))
            binding.itemSetMentoringScheduleDayOfWeek.setOnClickListener {
                publishItemClick.onNext(schedule)
                itemList.remove(schedule)
                notifyDataSetChanged()
            }
            val default="00:00"

            if(schedule.startTime!=null) binding.itemSetMentoringScheduleStartTime.setText(schedule.getTimeString(schedule.startTime!!))
            else binding.itemSetMentoringScheduleStartTime.setText(default)

            if(schedule.finishTime!=null) binding.itemSetMentoringScheduleFinishTime.setText(schedule.getTimeString(schedule.finishTime!!))
            else binding.itemSetMentoringScheduleFinishTime.setText(default)


            val startTimeListener=TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                binding.itemSetMentoringScheduleStartTime.setText("${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}")
                schedule.startTime= hourOfDay*60+minute
            }
            val finishTimeListener=TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                binding.itemSetMentoringScheduleFinishTime.setText("${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}")
                schedule.finishTime= hourOfDay*60+minute
            }
            val startTimeDialog=TimePickerDialog(itemView.context,startTimeListener,0,0,false)
            val finishTimeDialog=TimePickerDialog(itemView.context,finishTimeListener,0,0,false)


            binding.itemSetMentoringScheduleStartTime.setOnClickListener {
                startTimeDialog.show()
            }
            binding.itemSetMentoringScheduleFinishTime.setOnClickListener {
                finishTimeDialog.show()
            }
        }
    }

}