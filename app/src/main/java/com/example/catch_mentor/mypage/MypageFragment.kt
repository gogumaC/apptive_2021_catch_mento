package com.example.catch_mentor.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.databinding.FragmentMypageBinding
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager


class MypageFragment: BaseFragment() {

    private lateinit var binding: FragmentMypageBinding
    var PICK_IMAGE_FROM_ALBUM = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDataManager.userData.subscribe {
            when(it) {
                is Mentor -> {
                    binding.mypageName.setText(it.name)
                    binding.mypageText.setText("멘토님!")
                    binding.mypageButtonMentorStar.setVisibility(View.GONE)
                }
                is Mentee -> {
                    binding.mypageName.setText(it.name)
                    binding.mypageButtonMentorStar.setVisibility(View.VISIBLE)
                }
            }
        }
        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "마이 페이지"
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }


        val userData: Any =
            UserDataManager.getUserData() //멘토인지 멘티인지에 따라 any->Mentor, Mentee로 캐스팅해서 사용
        if (UserDataManager.isMentor()) {
            val mentorData: Mentor = userData as Mentor
            binding.mypageName.text = "${mentorData.name} "
        } else {
            val menteeData: Mentee = userData as Mentee
            binding.mypageName.text = "${menteeData.name} "
        }

        ServerProfile().imageDownload(requireContext(), UserDataManager.id!!, binding.mypageImage)

        binding.mypageButtonMypost.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mypageFragment_to_mypageMypostFragment)
        }

        binding.mypageButtonProfile.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mypageFragment_to_mypageProfileFragment)
        }

        binding.mypageButtonFavpost.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mypageFragment_to_mypageFavpostFragment)
        }

        binding.mypageButtonMentorStar.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_mypageFragment_to_mypageStarratingFragment)
        }

        binding.mypageImage.setOnClickListener {
            //cropImage(null)
        }
    }

//    fun cropImage(uri: Uri?){
//        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
//            .setCropShape(CropImageView.CropShape.RECTANGLE)
//            .start(requireActivity())
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("마이페이지", "xptmxm1")
//        when (requestCode){
//            PICK_IMAGE_FROM_ALBUM -> {
//                Log.d("마이페이지", "xptmxm2")
//
//                data?.data?.let { uri ->
//                    cropImage(uri) //이미지를 선택하면 여기가 실행됨
//                }
//                var photoUri = data?.data
//                binding.mypageImage.setImageURI(photoUri)
//            }
//            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                Log.d("마이페이지", "xptmxm3")
//                //그후, 이곳으로 들어와 RESULT_OK 상태라면 이미지 Uri를 결과 Uri로 저장!
//                val result = CropImage.getActivityResult(data)
//                if(resultCode == Activity.RESULT_OK){
//                    result.uri?.let {
//                        binding.mypageImage.setImageBitmap(result.bitmap)
//                        binding.mypageImage.setImageURI(result.uri)
//                        var photoUri = result.uri
//                        binding.mypageImage.setImageURI(photoUri)
//
//                    }
//                }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                }
//            }
//            else ->{}
//
//        }
//    }
}
