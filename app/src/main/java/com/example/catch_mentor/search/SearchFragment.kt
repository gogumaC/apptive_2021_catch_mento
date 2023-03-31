package com.example.catch_mentor.search

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.chatting.ChattingFragment
import com.example.catch_mentor.customView.CatchMentoBottomNavBar
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.databinding.FragmentSearchBinding
import com.example.catch_mentor.home.HomeFragmentDirections
import com.example.catch_mentor.search.SearchFragment.search.keywordList
import com.example.catch_mentor.search.SearchFragment.search.searchMode
import com.example.catch_mentor.serverClass.FavoriteListInteractor
import com.example.catch_mentor.signup.SignupFragmentMain
import com.example.catch_mentor.signup.SignupViewModel
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID
import com.example.catch_mentor.write.postID.documentFav
import com.example.catch_mentor.write.postID.documentID
import com.example.catch_mentor.write.postID.documentName
import com.example.catch_mentor.write.write
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

interface searchListener{
    fun postLoad(it: MutableList<WriteInfoS>)
}

class SearchFragment: BaseFragment(), searchListener {

    private lateinit var searchAdapter : SearchAdapter
    private lateinit var spinnerAdapter : ArrayAdapter<String>

    private val model: SearchViewModel by activityViewModels()

    private lateinit var binding: FragmentSearchBinding
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    private lateinit var searchKeywordAdapter : SearchKeywordAdapter

    var itemList : MutableList<WriteInfoS> = mutableListOf()

    var user = Firebase.auth.currentUser
    var catchmentor : Boolean = false
    var isM = ""
    object search{
        var keywordList : SearchInfo = SearchInfo("", "", "", mutableListOf(), mutableListOf()) //뷰모델 적용
        var searchMode: Boolean = false
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        binding= FragmentSearchBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = SearchAdapter(requireContext())
        searchKeywordAdapter = SearchKeywordAdapter()
        binding.searchMainFb.bringToFront()

        var sp = 0

        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)


        toolbar.setNavigationIcon(R.drawable.icon_back)

        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        val listObserver = Observer<MutableList<WriteInfoS>> {
            itemList = it
            postLoad(it)
        }

        model.itemList.observe(viewLifecycleOwner, listObserver)


        if(UserDataManager.isMentor()) {
            isM = "mentor_user"
            catchmentor = true
        }else {
            isM = "mentee_user"
            catchmentor = false
            binding.searchTextView1.text = "멘티 캐치"
        }


        if(keywordList.subject.isNotEmpty() || keywordList.region.isNotEmpty() || keywordList.group != "" || keywordList.online != "" || keywordList.sex != ""){
            Log.d("searchF", "키워드서치")
            search()
        }
        else{
            openDB(isM)
        }


        if(keywordList.subject.size > 0){
            binding.searchKeywordSubject.setBackgroundResource(R.drawable.style_keyword_button_selected)
            binding.searchKeywordSubject.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
        }else{
            binding.searchKeywordSubject.setBackgroundResource(R.drawable.style_keyword_button)
            binding.searchKeywordSubject.setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))
        }
        if(keywordList.region.size > 0){
            binding.searchKeywordRegion.setBackgroundResource(R.drawable.style_keyword_button_selected)
            binding.searchKeywordRegion.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
        }else{
            binding.searchKeywordSubject.setBackgroundResource(R.drawable.style_keyword_button)
            binding.searchKeywordSubject.setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))
        }


        searchKeywordAdapter.setOnItemClickListener(object : SearchKeywordAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {

                keywordList.subject.removeAt(pos)
                searchKeywordAdapter.datas = keywordList.subject
                searchKeywordAdapter.notifyDataSetChanged()
                search()

                }
            })
        /*
        필터: 과목 성별 지역 온라인 그룹여부
        */

        // 게시물 정보를 firebase에서 가져오기

        binding.searchList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchList.adapter = searchAdapter

        Log.d("서치", "$itemList")

        binding.searchSelectKeyword.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.searchSelectKeyword.adapter = searchKeywordAdapter


        var fbClick = false
         // true: 멘토 검색 false: 멘티 검색

        binding.searchNavBar.setOnCatchMentoBottomNavBarCallbackListener(object:CatchMentoBottomNavBar.CatchMentoBottomNavBarCallBackListener{
            override fun onButtonClicked(state: Int) {
                when(state){
                    CatchMentoBottomNavBar.MY_PAGE->{findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToMypageFragment())}
                }
            }
        })



        //플로팅 버튼 클릭시 이벤트
        binding.searchMainFb.setOnClickListener {
            fbClick = !fbClick
            if (fbClick) {
                binding.searchFbClickView.visibility = View.VISIBLE
                binding.searchCatchFb.visibility = View.VISIBLE
                binding.searchWriteFb.visibility = View.VISIBLE
                binding.searchCatchFbText.visibility = View.VISIBLE
                binding.searchWriteFbText.visibility = View.VISIBLE
                binding.searchCatchFbText.bringToFront()
                binding.searchWriteFbText.bringToFront()
                if(catchmentor){
                    binding.searchCatchFbText.text = "멘티 캐치"
                }else{
                    binding.searchCatchFbText.text = "멘토 캐치"
                }
            } else {
                binding.searchFbClickView.visibility = View.GONE
                binding.searchCatchFb.visibility = View.INVISIBLE
                binding.searchWriteFb.visibility = View.INVISIBLE
                binding.searchCatchFbText.visibility = View.INVISIBLE
                binding.searchWriteFbText.visibility = View.INVISIBLE
            }
        }

        binding.searchFbClickView.setOnClickListener{
            binding.searchFbClickView.visibility = View.GONE
            binding.searchCatchFb.visibility = View.INVISIBLE
            binding.searchWriteFb.visibility = View.INVISIBLE
            binding.searchCatchFbText.visibility = View.INVISIBLE
            binding.searchWriteFbText.visibility = View.INVISIBLE
        }


        binding.searchWriteFb.setOnClickListener {
            if(UserDataManager.isMentor()) {
                view.findNavController()
                    .navigate(R.id.action_searchFragment_to_writeMentorFragment)
            }else{
                view.findNavController()
                    .navigate(R.id.action_searchFragment_to_writeMenteeFragment)
            }
        }

        binding.searchEditText.setOnKeyListener { v, keyCode, event -> //Enter key Action
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && !(binding.searchEditText.text.toString()
                    .isEmpty())
            ) {
                val searchText = binding.searchEditText.text.toString()

                itemList.clear()
                model.itemList.value = itemList
                if (catchmentor) {
                    SearchInDB(this, model).searchText("mentor_post", searchText, true)
                }else {
                    SearchInDB(this, model).searchText("mentee_post", searchText, false)
                }
                true
            }else if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && (binding.searchEditText.text.toString()
                    .isEmpty())
            ) {
                openDB(isM)
                true
            }else {
                false
            }
        }


        binding.searchCatchFb.setOnClickListener {
            itemList.clear()
            model.itemList.value = itemList
            catchmentor = !catchmentor
            binding.searchFbClickView.visibility = View.GONE
            binding.searchCatchFb.visibility = View.INVISIBLE
            binding.searchWriteFb.visibility = View.INVISIBLE
            binding.searchCatchFbText.visibility = View.INVISIBLE
            binding.searchWriteFbText.visibility = View.INVISIBLE
            if(!catchmentor) {
                binding.searchTextView1.text = "멘티 캐치"
            }else{
                binding.searchTextView1.text = "멘토 캐치"
            }
            binding.searchList.removeAllViewsInLayout()
            openDB(isM)
        }


        searchAdapter.setOnItemClickListener(object: SearchAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                if(catchmentor) {
                    documentID = itemList[pos].docuID
                    documentName = itemList[pos].title
                    documentFav = itemList[pos].isFav
                    Log.d(ContentValues.TAG, documentID + " " + itemList[pos].docuID)
                    view.findNavController().navigate(R.id.action_searchFragment_to_postMentorFragment)
                }else{
                    documentID = itemList[pos].docuID
                    documentName = itemList[pos].title
                    documentFav = itemList[pos].isFav
                    Log.d(ContentValues.TAG, documentID + " " + itemList[pos].docuID)
                    view.findNavController().navigate(R.id.action_searchFragment_to_postMenteeFragment)
                }
            }

            override fun onFavClick(v: View, pos: Int) {
                itemList[pos].isFav = !itemList[pos].isFav
                model.itemList.value = itemList
                if(itemList[pos].isFav) {
                    FavoriteListInteractor(this).Favadd(user!!.uid, itemList[pos].docuID)
                }else{
                    FavoriteListInteractor(this).Favdel(user!!.uid, itemList[pos].docuID)
                }
            }

            override fun updateF(){
                binding.searchList.removeAllViewsInLayout()
                itemList.clear()
                model.itemList.value = itemList
                openDB(isM)
            }
        }
        )


        //키워드 선택부분

        binding.searchKeywordOnline.setOnClickListener {
            if(binding.searchKeywordOnline.isChecked) {
                binding.searchKeywordOnline.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                keywordList.online = binding.searchKeywordOnline.isChecked.toString()
            }else{
                binding.searchKeywordOnline.setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))
                keywordList.online = ""
            }
            search()
        }
        binding.searchKeywordGroup.setOnClickListener {
            if(binding.searchKeywordGroup.isChecked) {
                binding.searchKeywordGroup.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                keywordList.group = binding.searchKeywordGroup.isChecked.toString()
            }else{
                binding.searchKeywordGroup.setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))
                keywordList.group = ""
            }
            search()
        }


        binding.searchKeywordSubject.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchFragment_to_searchSubjectFragment)
        }

        binding.searchKeywordRegion.setOnClickListener {
            view.findNavController().navigate(R.id.action_searchFragment_to_searchAddress)
        }


        binding.searchKeywordSex.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.sexList,
            R.layout.item_spinner_text)

        binding.searchKeywordSex.dropDownVerticalOffset =
            TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            31F,
            resources.displayMetrics).toInt()

        binding.searchKeywordSex.setSelection(2, false)


        binding.searchKeywordSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    //여
                    0 -> {
                        binding.searchKeywordSex.setBackgroundResource(R.drawable.style_keyword_button_selected)
                        keywordList.sex = ("여")
                        search()
                    }
                    //남
                    1 -> {
                        binding.searchKeywordSex.setBackgroundResource(R.drawable.style_keyword_button_selected)
                        keywordList.sex = ("남")
                        search()
                    }
                    //상관없음
                    2 -> {
                        sp++
                        Log.d("서치", sp.toString())
                        binding.searchKeywordSex.setBackgroundResource(R.drawable.style_keyword_button)
                        if(keywordList.subject.isNotEmpty() || keywordList.region.isNotEmpty() || keywordList.group != "" || keywordList.online != ""){
                            Log.d("searchF", "키워드서치")
                            search()
                        }
                        if(sp > 1) openDB(isM)
                        keywordList.sex = ""
                    }
                    //일치하는게 없는 경우
                    else -> {
                    }
                }
            }
        }



    }


    override fun onResume() {
        super.onResume()

        Log.d("서치", "onResume")

        searchKeywordAdapter.datas = keywordList.subject
        if(keywordList.subject.size > 0){
            binding.searchKeywordIcon.visibility = View.VISIBLE
            binding.searchKeywordSubject.setBackgroundResource(R.drawable.style_keyword_button_selected)
            binding.searchKeywordSubject.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
        }else{
            binding.searchKeywordIcon.visibility = View.INVISIBLE
        }
        searchKeywordAdapter.notifyDataSetChanged()
  }

    override fun onDestroy() {
        super.onDestroy()
        keywordList = SearchInfo("", "", "", mutableListOf(), mutableListOf())
    }


    // backgroundTask를 실행하는 메소드
    override fun postLoad(it: MutableList<WriteInfoS>) {
        // task에서 반환할 Hashmap

        Log.d("BG 테스트", itemList.size.toString() + searchMode.toString())
        searchAdapter.setData(it)

        Log.d("BG", "완료")

    }

    fun search(){
        itemList.clear()
        model.itemList.value = itemList

        searchMode = true
        Log.d("searchF", "서치함수")
        if (catchmentor) {
            SearchInDB(this, model).searchKeyword("mentor_post")
        } else {
            SearchInDB(this, model).searchKeyword("mentee_post")
        }
    }


    fun openDB(isM : String){
        itemList.clear()
        model.itemList.value = itemList

        Log.d("BG 테스트2", itemList.size.toString() + "/" + "오픈db")
        if(catchmentor) {
            SearchUploadInteractor(this, model).SearchUpload("mentor_post", isM)
        }else{
            SearchUploadInteractor(this, model).SearchUpload("mentee_post", isM)
        }
    }


}

