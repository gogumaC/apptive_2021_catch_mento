package com.example.catch_mentor.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentAddressABinding
import com.example.catch_mentor.databinding.FragmentAddressBBinding
import com.example.catch_mentor.mypage.MypageProfileFragment.profileEdit.editM
import com.example.catch_mentor.post.PostAdapter
import com.example.catch_mentor.search.SearchAddress.address.listA
import com.example.catch_mentor.search.SearchAddress.address.listC
import com.example.catch_mentor.search.SearchAddress.address.listD
import com.example.catch_mentor.write.postID.writeMode
import com.example.catch_mentor.write.write

class SearchAddressB(listB: MutableList<String>): BaseFragment() {

    private lateinit var binding : FragmentAddressBBinding
    var listB = listB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddressBBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter =
            ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1,
                listB as List<CharSequence>
            )
        binding.addressList.adapter = adapter

        binding.addressList.setOnItemClickListener { parent, view, position, id ->
            if (editM) {
                listD = listB[position]

                val searchAddressC = SearchAddressC()
                val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.address_frame, searchAddressC).commit()
            } else {
                if (listC.size == 3) {
                    Toast.makeText(requireContext(), "지역은 3개까지만 설정할 수 있어요.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (listB.isNotEmpty()) {
                        listC.add(listB[position])
                        if (writeMode) {
                            if (!write.writeLocal.contains(listB[position]))
                                write.writeLocal.add(listB[position])
                        } else {
                            if (!SearchFragment.search.keywordList.region.contains(listB[position]))
                                SearchFragment.search.keywordList.region.add(listB[position])
                        }
                        val searchAddressA = SearchAddressA()
                        val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.address_frame, searchAddressA).commit()
                    }
                }
            }
        }
    }

}