package com.example.catch_mentor.baseClass

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseAdapter<VH:RecyclerView.ViewHolder,ITEM_T>: RecyclerView.Adapter<VH>() {


    var itemList= mutableListOf<ITEM_T>()
    val publishItemClick= BehaviorSubject.create<ITEM_T>()
    val publishClickData=BehaviorSubject.create<Any>()

    override fun getItemCount(): Int =itemList.size

    open fun refreshData(items:List<ITEM_T>){
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    open fun insertData(items:List<ITEM_T>){
        val pos=itemList.size
        itemList.addAll(items)
        notifyItemInserted(pos)
    }

    open fun deleteData(items: List<ITEM_T>){
        val pos=itemList.indexOf(items[0])
        itemList.removeAll(items)
        notifyItemRemoved(pos)
    }

    open fun deleteData(item:ITEM_T){
        val pos=itemList.indexOf(item)
        itemList.removeAt(pos)
        notifyItemRemoved(pos)
    }
}