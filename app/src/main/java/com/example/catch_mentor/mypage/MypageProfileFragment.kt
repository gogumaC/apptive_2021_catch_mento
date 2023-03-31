package com.example.catch_mentor.mypage

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.chatting.ChattingFragment
import com.example.catch_mentor.databinding.FragmentMypageProfileBinding
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.editM
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.profileAdd
import com.example.catch_mentor.post.PostAdapter
import com.example.catch_mentor.utils.UserDataManager
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MypageProfileFragment: BaseFragment() {

    private lateinit var binding: FragmentMypageProfileBinding
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    private var editMode = false
    var address = ""
    private lateinit var addressAdapter : PostAdapter


    object profileEdit{
        var editM = false
        var profileAdd = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageProfileBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar =
            requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "프로필 수정"
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        addressAdapter = PostAdapter()

        binding.mypageProfileFloattingBtn.setOnClickListener {
            if (editMode) {
                editMode = false
                if (UserDataManager.isMentor()) {
                    db.collection("mentor_user").document(UserDataManager.id!!).update(
                        "name", binding.mypageProfileName.text,
                        "region", binding.mypageProfileAddress.text,
                        "birth", binding.mypageProfileBirth.text,
                        "univ", binding.mypageProfileSchool.text,
                        "sex", binding.mypageProfileSex.text,
                        "major", binding.mypageProfileGrade.text,
                        "certificate", binding.mypageProfileCert.text,
                        "career", binding.mypageProfileCar.text
                    )
                } else {
                    db.collection("mentee_user").document(UserDataManager.id!!).update(
                        "name", binding.mypageProfileName.text,
                        "region", binding.mypageProfileAddress.text,
                        "birth", binding.mypageProfileBirth.text,
                        "school", binding.mypageProfileSchool.text,
                        "sex", binding.mypageProfileSex.text,
                        "grade", binding.mypageProfileGrade.text,
                    )
                }
                Toast.makeText(requireContext(), "프로필 수정이 완료되었어요.", Toast.LENGTH_SHORT).show()
                binding.mypageProfileFloattingBtn.text = "수정하기"
            } else {
                editMode = true
                binding.mypageProfileFloattingBtn.text = "저장하기"
            }
        }

        //설정
        if (UserDataManager.isMentor()) {
            binding.mypageProfileGradeTv.text = "학과"
            binding.mypageProfileCarTv.visibility = View.VISIBLE
            binding.mypageProfileCar.visibility = View.VISIBLE
            binding.mypageProfileCertTv.visibility = View.VISIBLE
            binding.mypageProfileCert.visibility = View.VISIBLE

            db.collection("mentor_user").document(UserDataManager.id!!).get().addOnSuccessListener {
                binding.mypageProfileName.text = it["name"] as String
                binding.mypageProfileEmail.text = it["email"] as String
                binding.mypageProfileBirth.text = it["birth"] as String
                binding.mypageProfileSchool.text = it["univ"] as String
                binding.mypageProfileSex.text = it["sex"] as String
                binding.mypageProfileGrade.text = it["major"] as String
                binding.mypageProfileCert.text = it["certificate"] as String
                address = it["region"] as String
                binding.mypageProfileAddress.text = it["region"] as String
            }
        } else {
            db.collection("mentee_user").document(UserDataManager.id!!).get().addOnSuccessListener {
                binding.mypageProfileName.text = it["name"] as String
                binding.mypageProfileEmail.text = it["email"] as String
                binding.mypageProfileBirth.text = it["birth"] as String
                binding.mypageProfileSchool.text = it["school"] as String
                binding.mypageProfileSex.text = it["sex"] as String
                binding.mypageProfileGrade.text = it["grade"] as String
                address = it["region"] as String
                binding.mypageProfileAddress.text = it["region"] as String
            }
        }
        binding.mypageProfileCar.setOnClickListener {
            if (editMode) {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.mypageProfileCar.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.mypageProfileCar.text = name
                    }

                })
            }
        }
        binding.mypageProfileCert.setOnClickListener {
            if (editMode) {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.mypageProfileCert.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.mypageProfileCert.text = name
                    }

                })

            }
        }


        binding.mypageProfileAddress.setOnClickListener {
            Log.d("프로필", "text")
            if (editMode) {
                editM = true
                view.findNavController()
                    .navigate(R.id.action_mypageProfileFragment_to_searchAddress)
            }
        }

        binding.mypageProfileName.setOnClickListener {

            if (editMode) {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.mypageProfileName.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.mypageProfileName.text = name
                    }

                })
            }
        }
        binding.mypageProfileSchool.setOnClickListener {
            if (editMode) {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.mypageProfileSchool.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.mypageProfileSchool.text = name
                    }

                })
            }
        }
        binding.mypageProfileSex.setOnClickListener {
            if (editMode) {

                val dialog = AlertDialog.Builder(requireContext())
                val sex = resources.getStringArray(R.array.sexMypageList)

                dialog.setItems(sex) { dialog, which ->
                    binding.mypageProfileSex.text = sex[which]
                }
                dialog.show()
            }
        }
        binding.mypageProfileBirth.setOnClickListener {
            if (editMode) {

                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dialog =
                    DatePickerDialog(
                        requireContext(),
                        object : DatePickerDialog.OnDateSetListener {
                            override fun onDateSet(
                                view: DatePicker?,
                                year: Int,
                                month: Int,
                                dayOfMonth: Int
                            ) {
                                binding.mypageProfileBirth.text =
                                    "${year}. ${month + 1}. ${dayOfMonth}"
                            }
                        },
                        year,
                        month,
                        date
                    )
                dialog.show()
            }
        }

        binding.mypageProfileGrade.setOnClickListener {
            if (editMode) {
                val dialog = ProfileDialog(requireContext())
                dialog.showDialog(binding.mypageProfileGrade.text.toString())
                dialog.setOnClickListener(object : ProfileDialog.OnDialogClickListener {
                    override fun onClicked(name: String) {
                        binding.mypageProfileGrade.text = name
                    }

                })
            }
        }



    }

    override fun onResume() {
        super.onResume()
        binding.mypageProfileAddress.text = profileAdd
        if(editMode) binding.mypageProfileFloattingBtn.text = "저장하기"
    }

    override fun onDetach() {
        super.onDetach()
        profileAdd = ""
    }
}