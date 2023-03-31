package com.example.catch_mentor.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSignupSubABinding
import com.example.catch_mentor.databinding.FragmentSignupSubBinding
import com.example.catch_mentor.signup.SignupFragmentMain.signup.mUser
import com.example.catch_mentor.signup.SignupFragmentMain.signup.sign
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupFragmentSub: BaseFragment() {
    private lateinit var binding: FragmentSignupSubBinding
    private val model: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupSubBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupSubMentorBtn.setOnClickListener {
            sign++
            model.signNum.setValue(sign)
        }
        binding.signupSubMenteeBtn.setOnClickListener {
            mUser = "mentee_user"
            sign++
        }


    }
}


