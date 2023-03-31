package com.example.catch_mentor.UserTypeTest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.customView.CatchMentoBottomNavBar
import com.example.catch_mentor.databinding.FragmentUserTypeTestBinding
import com.example.catch_mentor.mentoringRecord.CalendarRecordViewModel

class UserTypeTestFragment: BaseFragment() {
    private val model: UserTypeTestViewModel by lazy{
        ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserTypeTestViewModel() as T
            }
        }).get(UserTypeTestViewModel::class.java)
    }
    private lateinit var binding:FragmentUserTypeTestBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserTypeTestBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter=UserTypeTestViewPagerAdapter()
        binding.totalPage=0
        compositeDisposable.add(
            adapter.publishClickData.subscribe {
                val pos=(it as Pair<*,*>).first as Int
                val ans=it.second as Int
                model.setQuestionResult(pos,ans)
                binding.userTypeTestViewPager.setCurrentItem(binding.userTypeTestViewPager.currentItem+1)
                if(pos==adapter.itemList.lastIndex){
                    model.send()
                    Toast.makeText(requireContext(),"서버에 데이터 보내고 결과랑 같이 화면변경",Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.userTypeTestViewPager.apply {
            setAdapter(adapter)
            setUserInputEnabled(false)
            registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.userTypeTestProgress.setProgress(position+1)
                   // binding.userTypeTestProgressIndicator.setText("${position+1} / ${testQuestions.size}")
                    binding.thisPage=position+1
                }
            })
        }



//        binding.temp.setOnCatchMentoBottomNavBarCallbackListener(object : CatchMentoBottomNavBar.CatchMentoBottomNavBarCallBackListener{
//            override fun onButtonClicked(state: Int) {
//                Toast.makeText(requireContext(),"안농",Toast.LENGTH_SHORT).show()
//            }
//        })
        binding.userTypeTestStartBtn.setOnClickListener {
            binding.userTypeTestContentsContainer.setVisibility(View.VISIBLE)
            binding.userTypeTestStartContainer.setVisibility(View.GONE)
        }

        model.questionList.observe(viewLifecycleOwner,{
            adapter.refreshData(it)
            binding.userTypeTestProgress.setMax(it.size)
            binding.totalPage=it.size
        })
        model.testResult.observe(viewLifecycleOwner,{
           // findNavController().navigate(UserTypeTestFragmentDirections.actionUserTypeTestFragmentToUserTypeTestResultFragment())
        })

    }
}