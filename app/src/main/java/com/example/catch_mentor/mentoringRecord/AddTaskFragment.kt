package com.example.catch_mentor.mentoringRecord

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catch_mentor.BuildConfig
import com.example.catch_mentor.baseClass.BaseDialogFragment
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.Task
import com.example.catch_mentor.databinding.DialogAddTaskBinding
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DeviceUtil

class AddTaskFragment(private val menteeId:String?=null,private val mentoring: Mentoring): BaseDialogFragment() {

    private lateinit var binding:DialogAddTaskBinding
   // private val model:AddTaskViewModel by viewModels()

    private val model:AddTaskViewModel by lazy{
        ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddTaskViewModel(mentoring) as T
            }
        }).get(AddTaskViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.setMenteeId(menteeId)
        getDialog()?.getWindow()?.apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
           // setLayout(DeviceUtil.deviceDpWidth?:DeviceUtil.getDeviceDpWidth((requireActivity())), ViewGroup.LayoutParams.WRAP_CONTENT)

        }
        binding= DialogAddTaskBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
       // val deviceWidth = size.x
        params?.width = (DeviceUtil.getDevicePxWidth(requireActivity()) * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
    //@RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startCalendarDialog= DatePickerDialog(requireContext())
        val finishCalendarDialog=DatePickerDialog(requireContext())


        binding.addTaskCancelBtn.setOnClickListener {
            dismiss()
        }
        binding.addTaskSetStartDateBtn.setOnClickListener {
//            val calendarDialog=AlertDialog.Builder(requireContext()).create().apply{
//                getWindow()?.apply{
//                    setGravity(Gravity.BOTTOM)
//                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                }
//                setView(CalendarView(requireContext()))
//                this.show()

                startCalendarDialog.setOnDateSetListener { datePicker, year, month, date ->
                    val startDate=DateUtility.makeDate(year,month,date)
                    model.setStartDate(startDate)
                  //  datePicker.setMinDate(startDate.time)
                }
                startCalendarDialog.show()


        }

        binding.addTaskSetFinishDateBtn.setOnClickListener {
                finishCalendarDialog.setOnDateSetListener { datePicker, year, month, date ->
                    val finishDate=DateUtility.makeDate(year,month,date)
                    model.setFinishDate(finishDate)
                   // datePicker.setMinDate(finishDate.time)
                }
                finishCalendarDialog.show()
        }

        binding.addTaskComplete.setOnClickListener {
            model.complete(binding.addTaskInputContent.getText().toString())
        }

        model.startDate.observe(viewLifecycleOwner,{
            val res=DateUtility.formatDate(it,"MM.dd")
            binding.addTaskSetStartDateBtn.setText(res)
        })
        model.finishDate.observe(viewLifecycleOwner,{
            val res=DateUtility.formatDate(it,"MM.dd")
            binding.addTaskSetFinishDateBtn.setText(res)
        })
        model.publishViewChange.observe(viewLifecycleOwner,{
                this.dismiss()
                makeShortToast("저장성공")

        })
    }
}