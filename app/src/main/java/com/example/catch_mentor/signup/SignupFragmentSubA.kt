package com.example.catch_mentor.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSignupBinding
import com.example.catch_mentor.databinding.FragmentSignupSubABinding

class SignupFragmentSubA: BaseFragment() {
    private lateinit var binding: FragmentSignupSubABinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupSubABinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupSubATxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                SignupFragmentMain.signup.email = s.toString()
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                SignupFragmentMain.signup.email = s.toString()
            }
            override fun afterTextChanged(s: Editable) {
                SignupFragmentMain.signup.email = s.toString()
            }
        })

    }
}