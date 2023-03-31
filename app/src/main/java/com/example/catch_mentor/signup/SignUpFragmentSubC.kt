package com.example.catch_mentor.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSignupSubCBinding
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.editM
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.profileAdd
import com.example.catch_mentor.mypage.ProfileDialog
import com.example.catch_mentor.signup.SignupFragmentMain.signup.mUser
import com.example.catch_mentor.signup.SignupFragmentMain.signup.sign
import com.example.catch_mentor.signup.SignupFragmentMain.signup.signupList
import java.util.*

class SignupFragmentSubC: BaseFragment() {
    private lateinit var binding: FragmentSignupSubCBinding
    private val model: SignupViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupSubCBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(mUser == "mentor_user"){
            binding.signupSubCCert.visibility = View.VISIBLE
            binding.signupSubCCertTv.visibility = View.VISIBLE
            binding.signupSubCCarTv.visibility = View.VISIBLE
            binding.signupSubCCar.visibility = View.VISIBLE
        }

        val signObserver = Observer<Int> { _ ->

            if(sign==4){
                checkSignUp()
            }

        }

        model.signNum.observe(viewLifecycleOwner, signObserver)


        binding.signupSubCName.setOnClickListener {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.signupSubCName.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.signupSubCName.text = name
                        signupList.name = name
                    }

                })
        }
        binding.signupSubCSchool.setOnClickListener {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.signupSubCSchool.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.signupSubCSchool.text = name
                        signupList.school = name
                    }

                })
        }
        binding.signupSubCSex.setOnClickListener {
                val dialog = AlertDialog.Builder(requireContext())
                val sex = resources.getStringArray(R.array.sexMypageList)

                dialog.setItems(sex) { dialog, which ->
                    binding.signupSubCSex.text = sex[which]
                    signupList.sex = sex[which]
                }
                dialog.show()
        }
        binding.signupSubCBirth.setOnClickListener {

                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dialog =
                    DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
                            binding.signupSubCBirth.text = "${year}. ${month + 1}. ${dayOfMonth}"
                            signupList.birth = "${year}. ${month + 1}. ${dayOfMonth}"
                        }
                    }, year, month, date)
                dialog.show()
        }

        binding.signupSubCGrade.setOnClickListener{
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.signupSubCGrade.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.signupSubCGrade.text = name
                        signupList.grade = name
                    }

                })
            }

        binding.signupSubCCar.setOnClickListener {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.signupSubCCar.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.signupSubCCar.text = name
                        signupList.career = name
                    }

                })
        }
        binding.signupSubCCert.setOnClickListener {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.signupSubCCert.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.signupSubCCert.text = name
                        signupList.certificate = name
                    }

                })
        }
    }

    private fun checkSignUp(){
        if(binding.signupSubCBirth.length() > 0 && binding.signupSubCGrade.length() > 0 &&
                binding.signupSubCSchool.length() > 0 && binding.signupSubCSex.length() > 0 && binding.signupSubCName.length() > 0){
                    sign++
                    model.signNum.setValue(sign)
        }else Toast.makeText(requireContext(), "필수 사항을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        //TODO 초기화 부분 잘 다뤄야할듯??
    }

    override fun onDetach() {
        super.onDetach()
        profileAdd = ""
        signupList = Signup("", "", "", "", "", "", "")
    }

}
