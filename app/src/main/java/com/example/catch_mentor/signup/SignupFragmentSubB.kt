package com.example.catch_mentor.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSignupSubBBinding
import com.example.catch_mentor.signup.SignupFragmentMain.signup.pwd


class SignupFragmentSubB: BaseFragment() {
    private lateinit var binding: FragmentSignupSubBBinding
    private val model: SignupViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupSubBBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupSubBTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                pwd = s.toString()
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                pwd = s.toString()
            }
            override fun afterTextChanged(s: Editable) {
                pwd = s.toString()
            }
        })

    }
}


