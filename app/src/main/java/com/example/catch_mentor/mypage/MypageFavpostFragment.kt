package com.example.catch_mentor.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentMypageFavpostBinding
import com.example.catch_mentor.post.FavoriteListInteractorPost
import com.example.catch_mentor.search.SearchAdapter
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.ktx.Firebase

interface MypageFavpostListener{
    fun postLoad(list: MutableList<WriteInfoS>)
}

class MypageFavpostFragment:BaseFragment(), MypageFavpostListener {

    private lateinit var bindng : FragmentMypageFavpostBinding
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    var user = Firebase.auth.currentUser
    private lateinit var mypageFavpostAdapter : MypageFavpostAdapter
    var fav_list : MutableList<WriteInfoS> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindng = FragmentMypageFavpostBinding.inflate(LayoutInflater.from(requireContext()))
        return bindng.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.title = ""
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "찜한 게시물"
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        var isM = "mentee_user"
        if(UserDataManager.isMentor()){
            isM = "mentor_user"
        }
        mypageFavpostAdapter = MypageFavpostAdapter(requireContext())
        bindng.mypageFavpostList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bindng.mypageFavpostList.adapter = mypageFavpostAdapter

        favUp(isM, this)

        mypageFavpostAdapter.setOnItemClickListener(object : MypageFavpostAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                postID.documentID = fav_list[pos].docuID
                if(UserDataManager.isMentor()) {
                    requireView().findNavController()
                        .navigate(R.id.action_mypageFavpostFragment_to_postMenteeFragment)
                }else{
                    requireView().findNavController()
                        .navigate(R.id.action_mypageFavpostFragment_to_postMentorFragment)
                }
            }

            override fun onFavClick(v: View, pos: Int) {
                FavoriteListInteractorPost().favdel(user!!.uid, fav_list[pos].docuID)
                fav_list.removeAt(pos)
                favUp(isM, this@MypageFavpostFragment)
            }

            override fun updateF() {
                TODO("Not yet implemented")
            }
        })
    }

    override fun postLoad(list: MutableList<WriteInfoS>) {
        fav_list = list
        Log.d("마이페이지", "화이팅!!!")
        mypageFavpostAdapter.setData(list)
    }
}

fun favUp(isM : String, listener: MypageFavpostListener){
    var mCallback = listener
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    var user = Firebase.auth.currentUser

    var list : MutableList<String> = mutableListOf()
    var list2 : MutableList<WriteInfoS> = mutableListOf()
    db.collection(isM).document(user!!.uid).get()
        .addOnSuccessListener{
            list = it["favorite_post"] as MutableList<String>

            var k = "mentor_post"
            if (UserDataManager.isMentor()) {
                k = "mentee_post"
            }
            for (i in list) {
                db.collection(k).document(i).get().addOnSuccessListener { document ->
                    if(document.exists()){
                    val item = WriteInfoS(
                        document["title"] as String,
                        document["subTitle"] as String,
                        document["subject"] as String,
                        document["userGrade"] as String,
                        document["userName"] as String,
                        document.id,
                        document["userID"] as String,
                        true,
                        "0"
                    )
                    list2.add(item)
                    mCallback.postLoad(list2)
                }
                }
            }

        }
}