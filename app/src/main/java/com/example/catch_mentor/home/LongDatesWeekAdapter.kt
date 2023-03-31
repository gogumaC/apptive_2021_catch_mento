package com.example.catch_mentor.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.databinding.CustomLongDateBinding
import com.example.catch_mentor.databinding.ItemGroupWeekLongDatesBinding
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.clearTime
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class LongDatesWeekAdapter:BaseAdapter<LongDatesWeekAdapter.SimpleLongDataViewHolder, Date>() {

    val publishDateChange=BehaviorSubject.create<Date>()
    val pageCount=Int.MAX_VALUE
    var selectedDate:Date
        private set

    init{
        val cal=Calendar.getInstance()
        cal.clearTime()
        selectedDate=cal.getTime()
    }
    val firstPos=pageCount/2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleLongDataViewHolder {
        val binding= ItemGroupWeekLongDatesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SimpleLongDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimpleLongDataViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int =pageCount

    inner class SimpleLongDataViewHolder(val binding:ItemGroupWeekLongDatesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pos:Int){

            val cursor=posToWeekOfMonth(pos)
            val list=getWeeks(cursor.first,cursor.second,cursor.third)

            binding.apply{
                val datesView=listOf(longDateView1,longDateView2,longDateView3,longDateView4,longDateView5,longDateView6,longDateView7)
                datesView.forEachIndexed { index, longDateView ->
                    try{

                        if(list[index]!=null){
                            longDateView.setVisibility(VISIBLE)
                            longDateView.setDate(list[index]!!)
                        }else{
                            longDateView.setVisibility(INVISIBLE)
                        }

                        longDateView.changeSelected(list[index]?.equals(selectedDate)?:false)


                        longDateView.setOnClickListener {
                            list[index]?.let{
                                changeSelectedDate(it)
                            }
                        }


                        longDateView.changeSelected(longDateView.getDate()==selectedDate)
//                        cal.add(Calendar.DATE,1)
                    }catch(e:Exception){}
                    finally {
                        longDateView.setOnClickListener {
                            changeSelectedDate(longDateView.getDate())

                        }
                    }

                }
            }


        }

    }

    fun posToWeekOfMonth(pagePos:Int):Triple<Int,Int,Int>{

        val pos=pagePos-firstPos
        val cal=Calendar.getInstance(Locale("en","UK"))
        val weekCount=cal. getActualMaximum(Calendar.WEEK_OF_MONTH)
        val thisWeek=cal.get(Calendar.WEEK_OF_MONTH)

        val yearRes:Int
        val monthRes:Int
        val weekRes:Int

        when{

            pos<0->{
                var mPos=thisWeek+pos
                while(mPos<=0){
                    cal.add(Calendar.MONTH,-1)
                    mPos+=cal.getActualMaximum(Calendar.WEEK_OF_MONTH)

                }

                weekRes=mPos

            }
            pos>0->{
                var mPos=pos-(weekCount-thisWeek)
                while(mPos>0){
                    cal.add(Calendar.MONTH,1)
                    mPos-=cal.getActualMaximum(Calendar.WEEK_OF_MONTH)
                }
                weekRes=cal.getActualMaximum(Calendar.WEEK_OF_MONTH)+mPos

            }
            else->{

                weekRes=cal.get(Calendar.WEEK_OF_MONTH)
            }


        }
        yearRes=cal.get(Calendar.YEAR)
        monthRes=cal.get(Calendar.MONTH)

        return Triple(yearRes,monthRes,weekRes)

    }

    fun getWeeks(year:Int,month:Int,weekCount:Int):List<Date?>{

        val cal=Calendar.getInstance()
        cal.clearTime()
        val list= mutableListOf<Date?>()
        cal.set(Calendar.YEAR,year)
        cal.set(Calendar.MONTH,month)
        cal.set(Calendar.WEEK_OF_MONTH,weekCount)
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
        for(i in 0..6){
            if(cal.get(Calendar.MONTH)==month){
                list.add(i,cal.getTime())
            }else{
                list.add(i,null)
            }

            cal.add(Calendar.DATE,1)
        }
        return list
    }

    fun changeSelectedDate(date:Date){
        publishDateChange.onNext(date)
        selectedDate=date
        notifyDataSetChanged()
    }
}