package com.example.catch_mentor.mypage

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.catch_mentor.R
import org.w3c.dom.Text
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.starRatingUnitData
import com.example.catch_mentor.databinding.FragmentMypageStarratingGiveBinding
import com.example.catch_mentor.mentoringRecord.CalendarRecordViewModel
import com.example.catch_mentor.utils.DateUtility

class MypageStarratingGiveFragment:BaseFragment() {

    private val args:MypageStarratingGiveFragmentArgs by navArgs()

    private val model:  MypageStarratingGiveViewModel by lazy{
        ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return  MypageStarratingGiveViewModel(args.mentoring) as T//
            }
        }).get( MypageStarratingGiveViewModel::class.java)
    }
    private lateinit var binding: FragmentMypageStarratingGiveBinding
    private lateinit var mypageStarratingGiveAdapter: MypageStarratingGiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMypageStarratingGiveBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mentoring=args.mentoring

        mypageStarratingGiveAdapter = MypageStarratingGiveAdapter(requireContext(),mentoring)
        binding.mypageStarratingGiveList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.mypageStarratingGiveList.adapter = mypageStarratingGiveAdapter


        mypageStarratingGiveAdapter.setOnRatingBarChangeListener(object : MypageStarratingGiveAdapter.OnItemClickListener{
            override fun onRatingChange(pos: kotlin.Int, r: kotlin.Int,data:starRatingUnitData){ //r이 별점 pos아이템순서
                val dialog = Dialog(requireContext())


                dialog.setContentView(R.layout.dialog_chatting)
                dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setCancelable(true)
                dialog.show()

                dialog.findViewById<TextView>(R.id.dialog_chat_txt).text = r.toString()
                dialog.findViewById<TextView>(R.id.dialog_chat_txt2).text = "점으로"

                dialog.findViewById<Button>(R.id.dialog_chat_cancel).setOnClickListener {
                    dialog.dismiss()
                }

                dialog.findViewById<Button>(R.id.dialog_chat_fin).setOnClickListener {
                    val newData=data
                    newData.starRating=r.toLong()
                    if(data.id==null)model.createRating(newData)
                    else model.updateRating(newData)
                    android.widget.Toast.makeText(requireContext(), "테스트", android.widget.Toast.LENGTH_SHORT).show()

                    dialog.dismiss()
                }


            }
        })
        model.publishStarRatingList.observe(viewLifecycleOwner,{

            mypageStarratingGiveAdapter.setData(it.toMutableList())

        })
        model.publishMentorName.observe(viewLifecycleOwner,{
            mypageStarratingGiveAdapter.setMentorName(it)
        })
    }

}