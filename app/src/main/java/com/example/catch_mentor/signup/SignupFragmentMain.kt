package com.example.catch_mentor.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSignupBinding
import com.example.catch_mentor.signup.SignupFragmentMain.signup.email
import com.example.catch_mentor.signup.SignupFragmentMain.signup.sign
import com.example.catch_mentor.signup.SignupFragmentMain.signup.signupList
import java.util.regex.Pattern

class SignupFragmentMain: BaseFragment(){

    private lateinit var binding: FragmentSignupBinding
    private val model: SignupViewModel by activityViewModels()


    //TODO 회원가입 완료를 위한 변수
    object signup{
        var sign = 0
        var mUser = "mentor_user"
        var email = ""
        var pwd = ""
        var signupList = Signup("", "", "", "", "", "", "")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSignupBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signupFragmentSub = SignupFragmentSub()
        val signupFragmentSubA = SignupFragmentSubA()
        val signupFragmentSubB = SignupFragmentSubB()
        val signupFragmentSubC = SignupFragmentSubC()

        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.signup_frame, signupFragmentSub).commit()

        val signObserver = Observer<Int> { _ ->
            when(sign){
                1-> {
                    binding.signupFloattingBtn.visibility = View.VISIBLE
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.signup_frame, signupFragmentSubA).commit()
                    binding.signupProgress.progress += 25
                    binding.signupMainTxt1.visibility = View.GONE
                    binding.signupMainTxt2.visibility = View.GONE
                    binding.signupMainSubLayout.visibility = View.VISIBLE
                    binding.signupMainSubTxt1.visibility = View.VISIBLE
                }
                2-> {
                    if(emailCheck(email)){
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.signup_frame, signupFragmentSubB).commit()
                    binding.signupProgress.progress += 25
                    binding.signupMainSubTxt2.setText("비밀번호 작성 ")
                    }
                }
                3-> {
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.signup_frame, signupFragmentSubC).commit()
                    binding.signupProgress.progress += 25
                }
                5-> {
                    SignupDB(signupList).singupAuth()
                    Toast.makeText(requireContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    requireView().findNavController().navigate(R.id.action_signupFragment_to_launchFragment)
                    //TODO 이부분 뭔가... 실패했을때 가능성도 고려해야할거같음
                }
            }

        }

        model.signNum.observe(viewLifecycleOwner, signObserver)

        binding.signupFloattingBtn.setOnClickListener {
            if(sign!=4) sign++
            model.signNum.setValue(sign)
        }
    }

    private fun emailCheck(e: String): Boolean{
        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        return if (pattern.matcher(e).matches()) {
            true
        } else {
            Toast.makeText(requireContext(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            false
        }
    }


}

