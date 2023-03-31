package com.example.catch_mentor.mentoringRecord.mentoringRecordSetting

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.baseClass.RecyclerViewDecoration
import com.example.catch_mentor.databinding.FragmentMentoringRecordSettingBinding
import com.example.catch_mentor.mentoringRecord.CalendarRecordViewModel
import com.example.catch_mentor.search.SelectSubjectFragment
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.getDate2
import com.example.catch_mentor.utils.DateUtility.getMonth2
import com.example.catch_mentor.utils.DateUtility.getYear2
import com.example.catch_mentor.utils.DeviceUtil

class MentoringRecordSettingFragment:BaseFragment() {

    private val model: MentoringRecordSettingViewModel by lazy{
        ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MentoringRecordSettingViewModel(args.mentoringId) as T//
            }
        }).get(MentoringRecordSettingViewModel::class.java)
    }
    private lateinit var binding:FragmentMentoringRecordSettingBinding
    private val args:MentoringRecordSettingFragmentArgs by navArgs()
    private lateinit var mentoringId:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mentoringId=args.mentoringId
        binding= FragmentMentoringRecordSettingBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectSubjectDialog=SelectSubjectCategoryFragment()
        val startCalendarDialog= DatePickerDialog(requireContext())
        val dayOfWeekSelectAdapter=DayOfWeekSelectAdapter(DeviceUtil.getDevicePxWidth(requireActivity()))
        val setDetailMentoringScheduleAdapter=SetMentoringScheduleAdapter()

        compositeDisposable.addAll(
            dayOfWeekSelectAdapter.publishItemClick.subscribe {
                //setDetailMentoringScheduleAdapter.addMentoringSchedule(it)
                model.createNewSchedule(it)
            },
            setDetailMentoringScheduleAdapter.publishItemClick.subscribe {
                it.dayOfWeek?.let{ dow->
                    model.deleteSchedule(it)
                    dayOfWeekSelectAdapter.unSelect(dow)
                }

            },
            selectSubjectDialog.publishDialogDismissed.subscribe {

                model.setSubject(it.toString())
            }
        )
        startCalendarDialog.setOnDateSetListener { datePicker, year, month, date ->
            binding.mentoringRecordSettingSetMentoringStartDate.setText("$year.${month + 1}.$date")
            model.setStartDate(DateUtility.parseDate("$year/$month/$date"))
        }

        binding.mentoringRecordSettingToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.mentoringRecordSettingSelectSubject.setOnClickListener {
            selectSubjectDialog.show(this.childFragmentManager,"selectSubject")
        }

        binding.mentoringRecordSettingWeekSelect2.apply{
            addItemDecoration(RecyclerViewDecoration(2,2,context=requireContext()))
            setAdapter(dayOfWeekSelectAdapter)
            setLayoutManager(LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false))
        }

//        binding.mentoringRecordSettingTitle.apply{
//            setOnKeyListener { view, keyCode, keyEvent ->
//                if(keyEvent.getAction()== KeyEvent.ACTION_UP&&keyCode==KeyEvent.KEYCODE_ENTER){
//                    //TODO(서버에 일지 이름 변경)
//                }
//                false
//            }
//        }

        binding.mentoringRecordSettingTimeSetting.apply{
            addItemDecoration(RecyclerViewDecoration(0,0,0,5,context=requireContext()))
            setAdapter(setDetailMentoringScheduleAdapter)
            setLayoutManager(LinearLayoutManager(requireContext()))
        }

        binding.mentoringRecordSettingSetMentoringStartDate.setOnClickListener {
            startCalendarDialog.show()
        }

        binding.mentoringRecordSettingSave.setOnClickListener {
            val title=binding.mentoringRecordSettingTitle.text.toString()
            model.complete(title)
            findNavController().popBackStack()
        }
//        model.classDays.observe(viewLifecycleOwner,{
//            dayOfWeekSelectAdapter.refreshSelectDays(it.map{it.dayOfWeek!!}.toMutableList())
//        })
        model.title.observe(viewLifecycleOwner,{
            binding.mentoringRecordSettingTitle.setText(it)
        })
        model.mentoringSchedule.observe(viewLifecycleOwner,{
            setDetailMentoringScheduleAdapter.refreshData(it)
            dayOfWeekSelectAdapter.refreshSelectDays(it.map{it.dayOfWeek!!}.toMutableList())
        })
        model.startDate.observe(viewLifecycleOwner,{
            startCalendarDialog.updateDate(it.getYear2(),it.getMonth2(),it.getDate2())
             binding.mentoringRecordSettingSetMentoringStartDate.setText(DateUtility.formatDate(it,"MM.dd"))
        })
        model.subject.observe(viewLifecycleOwner,{

                binding.mentoringRecordSettingSelectSubject.setText(it.toString())


        })
    }
}