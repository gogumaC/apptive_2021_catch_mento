package com.example.catch_mentor.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentLoginBinding
import com.example.catch_mentor.utils.UserDataManager

class LoginFragment:BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val model: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginNextBtn.setOnClickListener {
            binding.loginInputEmailContainer.setVisibility(View.GONE)
            binding.loginInputPasswordContainer.setVisibility(View.VISIBLE)
        }
        binding.loginCompleteBtn.setOnClickListener {
            //로그인 시도
            model.login(requireActivity(),binding.loginEmailInput.getText().toString(),binding.loginPasswordInput.getText().toString())
        }

        binding.loginPasswordIsShow.setOnCheckedChangeListener { compoundButton, b ->
            if(b) binding.loginPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            else binding.loginPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance())
        }
        model.loginResult.observe(viewLifecycleOwner,{
            if(it.isEmpty()){
                Toast.makeText(requireContext(),"아이디 혹은 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
            else{
                //Toast.makeText(requireContext(),"로그인 성공 $it",Toast.LENGTH_LONG).show()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
        })
    }
}