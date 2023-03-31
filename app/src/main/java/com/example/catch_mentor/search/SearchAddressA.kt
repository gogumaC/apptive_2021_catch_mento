package com.example.catch_mentor.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentAddressABinding
import com.example.catch_mentor.search.SearchAddress.address.listC
import com.example.catch_mentor.write.postID.writeMode
import com.example.catch_mentor.write.write
import com.example.catch_mentor.write.write.writeLocal

class SearchAddressA():BaseFragment() {

    private lateinit var binding: FragmentAddressABinding
    private lateinit var searchAddressAdapter: SearchAddressAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddressABinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAddressAdapter = SearchAddressAdapter()
        binding.addressAList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.addressAList.adapter = searchAddressAdapter

        if(listC.isEmpty()) searchAddressAdapter.datas = writeLocal
            else searchAddressAdapter.datas = listC
        searchAddressAdapter.notifyDataSetChanged()
        Log.d("주소지", "${writeLocal} ${listC}")

        searchAddressAdapter.setOnItemClickListener(object : SearchAddressAdapter.OnDeleteClickListener {
            override fun onDeleteClick(v: View, pos: Int) {
                listC.removeAt(pos)

                if(writeMode) {
                    writeLocal.removeAt(pos)
                }else{
                    SearchFragment.search.keywordList.region.removeAt(pos)
                }

                searchAddressAdapter.datas = listC
                searchAddressAdapter.notifyDataSetChanged()
            }
        })

        binding.addressABtn.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }
    }
}