package com.example.catch_mentor.UserTypeTestResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentUserTypeTestResultBinding

class UserTypeTestResultFragment:BaseFragment() {

    private lateinit var binding:FragmentUserTypeTestResultBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserTypeTestResultBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}