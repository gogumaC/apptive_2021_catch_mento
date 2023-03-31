package com.example.catch_mentor.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentAddressBinding
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.editM
import com.example.catch_mentor.search.SearchAddress.address.listA
import com.example.catch_mentor.search.SearchAddress.address.listC
import com.example.catch_mentor.write.postID.writeMode


class SearchAddress: BaseFragment(){

    private lateinit var binding: FragmentAddressBinding
    val searchAddressA = SearchAddressA()
    val searchAddressC = SearchAddressC()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddressBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    object address{
        var listA = arrayOf(String())
        var listC = mutableListOf<String>()
        var listD = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        if(editM) {
            fragmentTransaction.add(R.id.address_frame, searchAddressC).commit()
        }else{
            fragmentTransaction.add(R.id.address_frame, searchAddressA).commit()
        }
        listA = resources.getStringArray(R.array.addressList)
        var listB: ArrayList<String> = arrayListOf()



        binding.addressEditText.setOnKeyListener { v, keyCode, event -> //Enter key Action
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && !(binding.addressEditText.text.toString()
                    .isEmpty())
            ) {

                listB.clear()

                for (A in listA) {
                    if (A.contains(binding.addressEditText.text.toString())) {
                        listB.add(A)
                    }
                }

                val searchAddressB = SearchAddressB(listB)
                fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.address_frame, searchAddressB).commit()

                true
            } else {
                false
            }
        }

    }

    override fun onDetach() {
        super.onDetach()
        listC.clear()
    }

}