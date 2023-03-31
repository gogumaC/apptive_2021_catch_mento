package com.example.catch_mentor.mentoringRecord

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.baseClass.BaseDialogFragment
import com.example.catch_mentor.dataClass.LearningRecord
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.databinding.DialogSetMentoringRecordBinding
import com.example.catch_mentor.mentoringRecord.mentoringRecordSetting.SetMentoringTasksAdapter
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.example.catch_mentor.utils.UserDataManager
import java.util.*

class SetMentoringRecordDialogFragment(val date: Date,private val menteeId:String?=null,private val learningRecord: LearningRecord?=null,val learningRecordId:String?=null,mentoring:Mentoring):BaseDialogFragment() {

    private lateinit var binding:DialogSetMentoringRecordBinding
    private val model:SetMentoringRecordViewModel by lazy{
        ViewModelProvider(this,object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SetMentoringRecordViewModel(mentoring,learningRecord,date) as T
            }
        }).get(SetMentoringRecordViewModel::class.java)
    }

    private val addTaskDialogFragment=AddTaskFragment(menteeId,mentoring)

    init{
        compostieDisposable.addAll(
            addTaskDialogFragment.publishDialogDismissed.subscribe {
                model.getDatas()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.getDatas()
        getDialog()?.getWindow()?.apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        binding= DialogSetMentoringRecordBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isMentor=UserDataManager.isMentor()
        binding.date= DateUtility.formatDate(date,"yyyy.MM.dd")

        val taskAdapter=SetMentoringTasksAdapter()
        compostieDisposable.addAll(
            taskAdapter.publishClickData.subscribe {
                addTaskDialogFragment.show(this.childFragmentManager,"addTask")
            },
            taskAdapter.publishItemClick.subscribe {
                Log.d("checkcheck",it.toString())
                model.changeTaskCompleteState(it)
            }
        )

        if(learningRecord==null){
            binding.setMentoringRecordTime.setVisibility(View.GONE)
        }
        if(learningRecord==null||!UserDataManager.isMentor()){
            binding.setMentoringRecordMentoringIsDone.setVisibility(View.GONE)
        }

        binding.setMentoringTasks.apply{
            setLayoutManager(LinearLayoutManager(requireContext()))
            setAdapter(taskAdapter)
        }


        binding.setMentoringRecordCancelBtn.setOnClickListener {
            dismiss()
        }
        binding.setMentoringRecordComplete.setOnClickListener {
            model.saveState(binding.setMentoringRecordMemo.text.toString())
            dismiss()
        }
        binding.setMentoringRecordMentoringIsDone.setOnCheckedChangeListener { compoundButton, isDone ->
            model.changeCompleteState(isDone)
        }
        model.publishLearningRecord.observe(viewLifecycleOwner,{
            binding.learningRecord=learningRecord
            binding.setMentoringRecordMentoringIsDone.setChecked(learningRecord?.isDone?:false)
        })
        model.tasks.observe(viewLifecycleOwner,{
            Log.d("tasks",it.toString())
            taskAdapter.refreshData(it)
        })
        model.memo.observe(viewLifecycleOwner,{
            binding.setMentoringRecordMemo.setText(it.memo)
        })
    }
}