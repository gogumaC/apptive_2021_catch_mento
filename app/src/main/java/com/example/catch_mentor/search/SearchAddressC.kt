package com.example.catch_mentor.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentAddressCBinding
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.editM
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.profileAdd
import com.example.catch_mentor.search.SearchAddress.address.listD

class SearchAddressC():BaseFragment() {

    private lateinit var binding: FragmentAddressCBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddressCBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressCBtn.setOnClickListener {
            binding.itemAddressCTxt.text = listD
            profileAdd = binding.itemAddressCTxt.text.toString()
            editM = false
            val navController = findNavController()
            navController.popBackStack()
        }
    }
}