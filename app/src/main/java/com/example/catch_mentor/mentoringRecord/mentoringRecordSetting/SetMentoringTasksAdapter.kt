package com.example.catch_mentor.mentoringRecord.mentoringRecordSetting

import android.R
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.dataClass.Task
import com.example.catch_mentor.databinding.ItemAddTaskBtnBinding
import com.example.catch_mentor.databinding.ItemTaskBinding
import com.example.catch_mentor.utils.DateUtility


class SetMentoringTasksAdapter:BaseAdapter<RecyclerView.ViewHolder, Task>() {

    val ADD_BUTTON=0
    val TASK=1

    override fun getItemViewType(position: Int): Int {
        return if(itemList.isNullOrEmpty())  ADD_BUTTON
        else TASK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0){
            val binding=ItemAddTaskBtnBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return AddTaskButtonViewHolder(binding)
        }else{
            val binding=ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            binding.root.setLayoutParams(ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            )
            return SetMentoringTasksViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AddTaskButtonViewHolder->{
                holder.setButton()
            }
            is SetMentoringTasksViewHolder->{
                holder.bind(itemList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if(itemList.isNullOrEmpty()) 1
            else super.getItemCount()
    }

    inner class AddTaskButtonViewHolder(val binding: ItemAddTaskBtnBinding):RecyclerView.ViewHolder(binding.root){
        fun setButton(){
            binding.addTaskBtn.apply{
                setOnClickListener {
                    publishClickData.onNext("")
                   // publishItemClick.onNext(Task())
                }
            }
        }
    }

    inner class SetMentoringTasksViewHolder(val binding:ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(task:Task){
            binding.task=task
            binding.checkBox.setChecked(task.isDone)
            if(task.startDate!=null&&task.finishDate!=null) {
                val time = DateUtility.formatDate(task.startDate, "MM.dd")+" ~ "+DateUtility.formatDate(task.finishDate, "MM.dd")
                binding.itemTaskSchedule.setText(time)
            }
            binding.checkBox.setOnCheckedChangeListener { compoundButton, b ->
                task.isDone=b
                publishItemClick.onNext(task)
            }
        }
    }

}