package com.example.catch_mentor.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSelectSubjectBinding
import com.example.catch_mentor.search.SearchFragment.search.keywordList
import com.example.catch_mentor.write.postID.writeMode
import com.example.catch_mentor.write.write.writeSubject


class SelectSubjectFragment: BaseFragment(){

    private lateinit var binding: FragmentSelectSubjectBinding
    private lateinit var searchKeywordAdapter : SearchKeywordAdapter
    private var livedata: MutableLiveData<MutableList<String>> = MutableLiveData()

    private lateinit var adapter1 : SubjectAdapter
    private lateinit var adapter2 : SubjectAdapter
    private lateinit var adapter3 : SubjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        binding = FragmentSelectSubjectBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
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
        toolbar.findViewById<TextView>(R.id.toolbar_ok_btn).visibility = View.VISIBLE
        toolbar.findViewById<TextView>(R.id.toolbar_ok_btn).setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }


        var sub1 = resources.getStringArray(com.example.catch_mentor.R.array.subjectList1)
        var sub2 = resources.getStringArray(com.example.catch_mentor.R.array.subjectList2)
        var sub3 = resources.getStringArray(com.example.catch_mentor.R.array.subjectList3)

        var click1 = false
        var click2 = false
        var click3 = false



        searchKeywordAdapter = SearchKeywordAdapter()
        binding.selectSubjectList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.selectSubjectList.adapter = searchKeywordAdapter
        livedata.observe(requireActivity(), {
            searchKeywordAdapter.datas = it
            searchKeywordAdapter.notifyDataSetChanged()
            Log.d("키워드", "완료 $it")
        })

        livedata.value = keywordList.subject

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
//
//        출처: https://leveloper.tistory.com/180 [꾸준하게]

        adapter1 = SubjectAdapter()
        adapter2 = SubjectAdapter()
        adapter3 = SubjectAdapter()

        binding.selectSubject1List.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.selectSubject2List.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.selectSubject3List.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.selectSubject1List.addItemDecoration(decoration)
        binding.selectSubject2List.addItemDecoration(decoration)
        binding.selectSubject3List.addItemDecoration(decoration)
        binding.selectSubject1List.adapter = adapter1
        binding.selectSubject2List.adapter = adapter2
        binding.selectSubject3List.adapter = adapter3
        adapter1.setData(sub1.toMutableList())
        adapter2.setData(sub2.toMutableList())
        adapter3.setData(sub3.toMutableList())


        binding.selectSubject1.setOnClickListener {
            if(binding.selectSubject1.isChecked) {
                binding.selectSubject1List.visibility = View.VISIBLE
                adapter1.notifyDataSetChanged()
            }else{
                binding.selectSubject1List.visibility = View.GONE
            }
        }

        binding.selectSubject2.setOnClickListener {
            if(binding.selectSubject2.isChecked) {
                binding.selectSubject2List.visibility = View.VISIBLE
                adapter2.notifyDataSetChanged()
            }else{
                binding.selectSubject2List.visibility = View.GONE
            }        }

        binding.selectSubject3.setOnClickListener {
            if(binding.selectSubject3.isChecked) {
                binding.selectSubject3List.visibility = View.VISIBLE
                adapter3.notifyDataSetChanged()
            }else{
                binding.selectSubject3List.visibility = View.GONE
            }        }


        adapter1.setOnItemClickListener(object : SubjectAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                if (writeMode) {
                    writeSubject = sub1.get(pos).toString()
                    writeMode = false
                    findNavController().popBackStack()
                }else { //검색일 경우
                    if (!keywordList.subject.contains(sub1[pos])) {
                        keywordList.subject.add(sub1.get(pos).toString())
                        livedata.value = keywordList.subject
                    }
            }
        }
        })

        adapter2.setOnItemClickListener(object : SubjectAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                if (writeMode) {
                    writeSubject = sub2.get(pos).toString()
                    writeMode = false
                    findNavController().popBackStack()
                }else { //검색일 경우
                    if (!keywordList.subject.contains(sub2[pos])) {
                        keywordList.subject.add(sub2.get(pos).toString())
                        livedata.value = keywordList.subject
                    }
                }
            }
        })

        adapter3.setOnItemClickListener(object : SubjectAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                if (writeMode) {
                    writeSubject = sub3[pos].toString()
                    writeMode = false
                    findNavController().popBackStack()
                }else { //검색일 경우
                    if (!keywordList.subject.contains(sub3[pos])) {
                        keywordList.subject.add(sub3.get(pos).toString())
                        livedata.value = keywordList.subject
                    }
                }
            }
        })

        searchKeywordAdapter.setOnItemClickListener(object : SearchKeywordAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                keywordList.subject.removeAt(pos)
                if(keywordList.subject.size == 0){
                    //TODO(키워드 스피너 원상복귀 시키기)
                }
                searchKeywordAdapter.notifyDataSetChanged()

            }
        }
        )
    }

}