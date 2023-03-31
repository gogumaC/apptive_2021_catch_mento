package com.example.catch_mentor.mentoringRecord

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.customView.CatchMentoBottomNavBar
import com.example.catch_mentor.customView.CatchMentoBottomNavBar.Companion.HOME
import com.example.catch_mentor.customView.CatchMentoBottomNavBar.Companion.MY_PAGE
import com.example.catch_mentor.customView.CatchMentoBottomNavBar.Companion.PROGRESS
import com.example.catch_mentor.dataClass.LearningRecord
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.MentoringSchedule
import com.example.catch_mentor.dataClass.Progress
import com.example.catch_mentor.databinding.FragmentCalendarRecordBinding
import com.example.catch_mentor.home.HomeFragment
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.example.catch_mentor.utils.DateUtility.clearTime
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import com.example.catch_mentor.utils.DeviceUtil
import com.example.catch_mentor.utils.UserDataManager
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.sql.Date
import java.sql.Timestamp
import java.time.DayOfWeek
import java.util.*

class CalendarRecordFragment:BaseFragment() {
    private val model:CalendarRecordViewModel by lazy{
        ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CalendarRecordViewModel(args.mentoring) as T//
            }
        }).get(CalendarRecordViewModel::class.java)
    }
    private lateinit var binding:FragmentCalendarRecordBinding
    private val args:CalendarRecordFragmentArgs by navArgs()

    private var proceeding=0
    private var done=0
    private var notYet=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        model.setMentoring(args.mentoring)
        model.setMenteeId(args.mentoring.menteeId)
        model.setMentorId(args.mentoring.mentorId)
        binding= FragmentCalendarRecordBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.calendarRecordCalendarView.apply{

            this.removeAllEvents()
            proceeding=0
            notYet=0
            done=0
            var dateCursor:java.util.Date?=null
            shouldDrawIndicatorsBelowSelectedDays(true)
            setFirstDayOfWeek(Calendar.SUNDAY)
            setLocale(TimeZone.getDefault(),Locale.KOREAN)
            setUseThreeLetterAbbreviation(true)

            setDefaultMentoringSchedules(this.firstDayOfCurrentMonth,args.mentoring)
            model.getLearningRecord(binding.calendarRecordCalendarView.firstDayOfCurrentMonth)
            val res= DateUtility.formatDate(this.firstDayOfCurrentMonth,"yyyy년 MM월")
            binding.calendarRecrodDayOfWeek.setText(res)

            setListener(object:CompactCalendarView.CompactCalendarViewListener{
                override fun onDayClick(dateClicked: java.util.Date?) {
                    if(dateClicked==dateCursor){
                        dateClicked?.let{
                            if(!getEvents(it).isNullOrEmpty()) {
                                try{
                                    SetMentoringRecordDialogFragment(
                                        it,
                                        args.mentoring.menteeId,
                                        getEvents(it)[0].data as LearningRecord,
                                        mentoring = args.mentoring
                                    ).show(
                                        this@CalendarRecordFragment.childFragmentManager,
                                        "setDialog"
                                    )
                                }catch(e:Exception){
                                    SetMentoringRecordDialogFragment(
                                        it,
                                        args.mentoring.menteeId,
                                        null,
                                        mentoring = args.mentoring
                                    ).show(
                                        this@CalendarRecordFragment.childFragmentManager,
                                        "setDialog"
                                    )
                                }

                            }
                            else {
                                SetMentoringRecordDialogFragment(
                                    it,
                                    args.mentoring.menteeId,
                                    null,
                                    mentoring = args.mentoring
                                ).show(
                                    this@CalendarRecordFragment.childFragmentManager,
                                    "setDialog"
                                )
                            }

                        }
                    }else{
                        dateCursor=dateClicked
                    }


                }

                override fun onMonthScroll(firstDayOfNewMonth: java.util.Date?) {
                   // val dayOfWeek=firstDayOfNewMonth
                    firstDayOfNewMonth?.let{
                        proceeding=0
                        notYet=0
                        done=0
                        setDefaultMentoringSchedules(it,args.mentoring)
                        model.getLearningRecord(it)
                        val res= DateUtility.formatDate(it,"yyyy년 MM월")
                        binding.calendarRecrodDayOfWeek.setText(res)
                    }

                }
            })

        }

        binding.calendarRecordToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.calendarRecordSettingBtn.apply {
            if(UserDataManager.isMentor()){
                setOnClickListener {
                    findNavController().navigate(
                        CalendarRecordFragmentDirections.actionCalendarRecordFragmentToMentoringRecordSettingFragment(
                            args.mentoring.id.toString()
                        )
                    )
                }
            }else{
                setVisibility(View.GONE)
            }

        }

        binding.calendarRecordChangePrevMonth.setOnClickListener {
            binding.calendarRecordCalendarView.scrollLeft()
        }
        binding.calendarRecordChangeNextMonth.setOnClickListener {
            binding.calendarRecordCalendarView.scrollRight()
        }

        binding.calendarRecordBottomNavBar.setOnCatchMentoBottomNavBarCallbackListener(object:CatchMentoBottomNavBar.CatchMentoBottomNavBarCallBackListener{
            override fun onButtonClicked(state: Int) {
                when(state){
                    HOME->findNavController().popBackStack(R.id.homeFragment,false)
                    MY_PAGE->findNavController().navigate(CalendarRecordFragmentDirections.actionGlobalMypageStarratingFragment())
                    PROGRESS->findNavController().popBackStack()
                }
            }
        })
        model.publishLearningRecords.observe(viewLifecycleOwner,{
            //binding.calendarRecordCalendarView.removeAllEvents()//apply{}

            it.forEach{
                if(it.isDone?:false){
                    if(binding.calendarRecordCalendarView.getEvents(it.date)!=null){
                        //TODO 뷰모델로 옮길듯
                        if(it.date?.after(Date())?:false)notYet--
                        else proceeding--
                        binding.calendarRecordCalendarView.removeEvents(it.date)
                    }
                    binding.calendarRecordCalendarView.addEvent(Event(ContextCompat.getColor(requireContext(), R.color.progress_done),it.date?.time?:0,it))
                    done++

                }

            }

            binding.progressRecordGraph.setProgress(Progress(notYet,proceeding,done))
            model.setProgress(Progress(notYet,proceeding,done),binding.calendarRecordCalendarView.firstDayOfCurrentMonth)


        })

        model.publishTasks.observe(viewLifecycleOwner,{
            val dates=it.map{it.startDate?.clearTime()}.toSet()//.toList()
            dates.forEach{
                binding.calendarRecordCalendarView.addEvent(Event(ContextCompat.getColor(requireContext(), R.color.task),it?.time?:0,it))
            }


        })

    }

    fun CompactCalendarView.setDefaultMentoringSchedules(startDate:java.util.Date, mentoring: Mentoring){
        val finishDate=startDate.changeDate(Calendar.MONTH,1)
        var date=startDate
        binding.calendarRecordCalendarView.removeAllEvents()
        val mentoringDayOfWeek:List<DayOfWeek?> =(mentoring.schedule?:listOf()).map{schedule->
//            val schedule:MentoringSchedule=it
            schedule.dayOfWeek
        }
        val events=mutableListOf<Event>()


        while(date.before(finishDate)){
            if(mentoringDayOfWeek.contains(date.getDayOfWeek())) {
                Log.d("checkfor","&&&&&&&&&&&&&&&&&&"+mentoringDayOfWeek.toString()+" : "+date.toString())
                if(date.before(Date())) {
                    events.add(
                        Event(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.progress_proceeding
                            ), date.time, LearningRecord()
                        )
                    )
                    proceeding++
                }
                else {
                    events.add(
                        Event(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.progress_not_yet
                            ), date.time, LearningRecord()
                        )
                    )
                    notYet++
                }
            }
            date=date.changeDate(Calendar.DATE,1)
        }
        addEvents(events)

    }

}