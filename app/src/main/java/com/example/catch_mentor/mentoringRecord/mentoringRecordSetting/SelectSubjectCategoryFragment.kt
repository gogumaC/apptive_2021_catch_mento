package com.example.catch_mentor.mentoringRecord.mentoringRecordSetting


import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.baseClass.BaseDialogFragment
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentSearchBinding
import com.example.catch_mentor.databinding.FragmentSelectSubjectBinding
import com.example.catch_mentor.search.SearchFragment.search.keywordList
import com.example.catch_mentor.search.SearchKeywordAdapter
import com.example.catch_mentor.search.SubjectAdapter
import com.example.catch_mentor.serverClass.UserClassificationInteractor
import com.example.catch_mentor.utils.DeviceUtil
import com.example.catch_mentor.utils.DeviceUtil.dpToPx
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.postID.writeMode
import com.example.catch_mentor.write.write.writeSubject
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.rpc.context.AttributeContext

open class SelectSubjectCategoryFragment : BaseDialogFragment(){

        private lateinit var binding: FragmentSelectSubjectBinding
        private lateinit var searchKeywordAdapter : SearchKeywordAdapter


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

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        // val deviceWidth = size.x
        params?.width = (DeviceUtil.getDevicePxWidth(requireActivity()) * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.selectSubjectLinearLayout.setPadding(0,0,0,20.dpToPx(requireContext()).toInt())
            val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(com.example.catch_mentor.R.id.catchmentor_toolbar)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            toolbar.setNavigationIcon(com.example.catch_mentor.R.drawable.icon_back)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbar.setNavigationOnClickListener {
                this.dismiss()
            }
            toolbar.findViewById<TextView>(com.example.catch_mentor.R.id.toolbar_ok_btn).visibility = View.VISIBLE
            toolbar.findViewById<TextView>(com.example.catch_mentor.R.id.toolbar_ok_btn).setOnClickListener {
                this.dismiss()
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


            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)



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
                click1 = !click1
                if(click1) {
                    binding.selectSubject1List.visibility = View.VISIBLE

                    adapter1.notifyDataSetChanged()
                }else{
                    binding.selectSubject1List.visibility = View.GONE
                }
            }

            binding.selectSubject2.setOnClickListener {
                click2 = !click2
                if(click2) {
                    binding.selectSubject2List.visibility = View.VISIBLE
                    adapter2.notifyDataSetChanged()
                }else{
                    binding.selectSubject2List.visibility = View.GONE
                }        }

            binding.selectSubject3.setOnClickListener {
                click3 = !click3
                if(click3) {
                    binding.selectSubject3List.visibility = View.VISIBLE
                    adapter3.notifyDataSetChanged()
                }else{
                    binding.selectSubject3List.visibility = View.GONE
                }        }


            adapter1.setOnItemClickListener(object : SubjectAdapter.OnItemClickListener{
                override fun onItemClick(v: View, pos: Int) {
                        if (!keywordList.subject.contains(sub1[pos])) {
                            backToSetting(sub1.get(pos).toString())
                        }
                    }

            })

            adapter2.setOnItemClickListener(object : SubjectAdapter.OnItemClickListener{
                override fun onItemClick(v: View, pos: Int) {

                        if (!keywordList.subject.contains(sub2[pos])) {

                            backToSetting(sub2.get(pos).toString())
                        }
                    }

            })

            adapter3.setOnItemClickListener(object : SubjectAdapter.OnItemClickListener{
                override fun onItemClick(v: View, pos: Int) {

                        if (!keywordList.subject.contains(sub3[pos])) {
                            backToSetting(sub3.get(pos).toString())
                            Log.d("checkcheck",sub3.get(pos).toString())

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

    fun backToSetting(subject:String){
        publishDialogDismissed.onNext(subject)
        this.dismiss()
    }


}

