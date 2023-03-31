package com.example.catch_mentor.mentoringRecord.mentoringList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.baseClass.RecyclerViewDecoration
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.databinding.FragmentMentoringListBinding
import com.example.catch_mentor.utils.DeviceUtil
import com.example.catch_mentor.utils.DeviceUtil.dpToPx
import com.example.catch_mentor.utils.UserDataManager

class MentoringListFragment: BaseFragment() {

    private val model:MentoringListViewModel by viewModels()
    private lateinit var binding:FragmentMentoringListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMentoringListBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerHeight= DeviceUtil.getDevicePxHeight(requireActivity())-144.dpToPx(requireContext())
        val mentoringListAdapter=MentoringListAdapter(recyclerHeight.toInt()).apply{
            compositeDisposable.addAll(
                publishItemClick.subscribe {
                    findNavController().navigate(MentoringListFragmentDirections.actionMentoringListFragmentToCalendarRecordFragment(mentoring=it))
                }
            )
        }

        binding.mentoringListToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.mentoringListRecyclerView.apply{
            addItemDecoration(RecyclerViewDecoration(2,2,0,10,requireContext()))
            setLayoutManager(LinearLayoutManager(requireContext()))
            setAdapter(mentoringListAdapter)
        }

        model.publishMentoring.observe(viewLifecycleOwner,{
            mentoringListAdapter.refreshData(it)
        })



    }
}