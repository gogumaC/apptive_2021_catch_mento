package com.example.catch_mentor.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.databinding.FragmentMypageStarratingBinding
import com.example.catch_mentor.mentoringRecord.CalendarRecordViewModel
import com.example.catch_mentor.utils.UserDataManager

class MypageStarratingFragment: BaseFragment() {

    private val model: MypageStarratingViewModel by lazy{
        ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MypageStarratingViewModel() as T//
            }
        }).get(MypageStarratingViewModel::class.java)
    }
    private lateinit var binding:FragmentMypageStarratingBinding
    private lateinit var mypageStarratingAdapter: MypageStarratingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMypageStarratingBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mypageStarratingAdapter = MypageStarratingAdapter(requireContext())
        binding.mypageStarratingList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.mypageStarratingList.adapter = mypageStarratingAdapter

        UserDataManager.userData.subscribe {
            when(it) {
                is Mentor -> {
                    binding.mypageStarratingNickname.setText(it.name)
                }
                is Mentee -> {
                    binding.mypageStarratingNickname.setText(it.name)
                }
            }
        }
        mypageStarratingAdapter.setOnItemClickListener(object : MypageStarratingAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                val mentoring=mypageStarratingAdapter.datas[pos]
                view.findNavController()
                    .navigate(MypageStarratingFragmentDirections.actionMypageStarratingFragmentToMypageStarratingGiveFragment(mentoring))

            }
        })


        model.publishMentoring.observe(viewLifecycleOwner,{

            mypageStarratingAdapter.setData(it.toMutableList())

        })


    }
}