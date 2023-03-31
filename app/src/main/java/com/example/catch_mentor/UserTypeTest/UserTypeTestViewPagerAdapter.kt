package com.example.catch_mentor.UserTypeTest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.databinding.ItemUserTypeTestBinding

class UserTypeTestViewPagerAdapter:BaseAdapter<UserTypeTestViewPagerAdapter.UserTypeTestViewHolder,Question>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTypeTestViewHolder {
        val binding=ItemUserTypeTestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserTypeTestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserTypeTestViewHolder, position: Int) {
        holder.bind(itemList[position],position)
    }

    inner class UserTypeTestViewHolder(private val binding:ItemUserTypeTestBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(question: Question,pos:Int){
            binding.itemUserTypeTestQuestion.setText(question.question)
            binding.itemUserTypeTestAns1.apply{
                setText(question.ans1)
                setOnClickListener {
                    publishClickData.onNext(pos to 0)
                }
            }
            binding.itemUserTypeTestAns2.apply{
                setText(question.ans2)
                setOnClickListener {
                    publishClickData.onNext(pos to 1)
                }
            }

        }
    }
}

data class Question(val question:String,val ans1:String, val ans2:String)