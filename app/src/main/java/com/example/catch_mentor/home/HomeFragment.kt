package com.example.catch_mentor.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
//import com.example.catch_mentor.UserTypeTest.UserTypeTestFragmentDirections
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.customView.CatchMentoBottomNavBar
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.dataClass.Progress
import com.example.catch_mentor.databinding.FragmentHomeBinding
import com.example.catch_mentor.interactors.LearningRecrodInteractor
import com.example.catch_mentor.interactors.MenteeInteractor
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.clearTime
import com.example.catch_mentor.utils.DateUtility.getFieldData
import com.example.catch_mentor.utils.DeviceUtil
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.utils.UserDataManager.MENTOR
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import java.util.*

class HomeFragment: BaseFragment() {

    private val model:HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DeviceUtil.getDeviceDpWidth(requireActivity())

        val progressCardAdapter=ProgressCardAdapter()
        val dateAdatper=LongDatesWeekAdapter()
        binding.year=Date().getFieldData(Calendar.YEAR)
        binding.month=Date().getFieldData(Calendar.MONTH)+1
        binding.weekCount=Date().getFieldData(Calendar.DATE)



        compositeDisposable.addAll(

            dateAdatper.publishDateChange.subscribe {
                Log.d("checkfor",it.toString())
                model.getLearningRecords(it)


            }
        )

        binding.homeProgressCardViewPager.apply{
            setAdapter(progressCardAdapter)

        }

//        binding.homeProgressCardPageIndicatorView.apply{
        binding.homeProgressCardPageIndicatorView.apply{
            setViewPager2(binding.homeProgressCardViewPager)
        }
        binding.homeBottomNavBar.setOnCatchMentoBottomNavBarCallbackListener(object:CatchMentoBottomNavBar.CatchMentoBottomNavBarCallBackListener{
            override fun onButtonClicked(state: Int) {
                when(state){
                    CatchMentoBottomNavBar.MY_PAGE->{findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMypageFragment())}
                    CatchMentoBottomNavBar.PROGRESS->{findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCalendarRecordFragment())}
                }

            }
        })

        binding.homeNoti.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToChattingListFragment())
        }
        binding.homeSearchBtn.setOnClickListener{
            //Toast.makeText(requireContext(),"검색으로",Toast.LENGTH_SHORT).show()
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())

        }


        binding.homeDateRecyclerView.apply{
            dateAdatper.refreshData(DateUtility.getNextDays())
            setAdapter(dateAdatper)
            setCurrentItem(dateAdatper.firstPos)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val cursor=dateAdatper.posToWeekOfMonth(position)
                    binding.year=cursor.first
                    binding.month=cursor.second+1
                    binding.weekCount=cursor.third
                }
            })
        }

        model.records.observe(viewLifecycleOwner,{
            progressCardAdapter.refreshData(it)
        })


        UserDataManager.userData.subscribe {
            when(it){
                is Mentor ->{
                    binding.isMento=true
                    binding.userName=it.name
                }
                is Mentee ->{
                    binding.isMento=false
                    binding.userName=it.name
                }
            }
            model.getMentoring()

        }



    }

    //backpress구현
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity(requireActivity())
                System.runFinalization();
                System.exit(0);
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

        override fun onDetach() {
            super.onDetach()
            callback.remove()
        }

}