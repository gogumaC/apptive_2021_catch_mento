package com.example.catch_mentor.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentLaunchBinding

class LaunchFragment: BaseFragment() {

    private lateinit var binding:FragmentLaunchBinding
    private val model:LaunchViewModel  by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLaunchBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.launchLogin.setOnClickListener {
            findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToLoginFragment())
        }

        binding.launchSignup.setOnClickListener {
            //회원가입으로 이동
            findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToSignupFragment())
        }

        model.loginResult.observe(viewLifecycleOwner,{
            findNavController().navigate(LaunchFragmentDirections.actionGlobalHomeFragment())
        })

    }
}