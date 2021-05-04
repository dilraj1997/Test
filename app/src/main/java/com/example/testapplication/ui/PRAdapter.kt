package com.example.testapplication.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.ErrorContainerBinding
import com.example.testapplication.databinding.LoadingContainerBinding
import com.example.testapplication.databinding.PrContainerBinding
import java.lang.IllegalStateException

class PRAdapter(val errorClickListener: () -> Unit) : RecyclerView.Adapter<PRBaseViewHolder>() {
    private var mDataList = listOf<PRItemType>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRBaseViewHolder {
        return when (viewType) {
            1 -> PRLoadingHolder(LoadingContainerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.loading_container, parent, false)))
            2 -> PRViewHolder(PrContainerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.pr_container, parent, false)))
            3 -> PRErrorHolder(ErrorContainerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.error_container, parent, false)), errorClickListener)
            else -> throw IllegalStateException("wrong view-holder type passed")
        }
    }

    override fun onBindViewHolder(holder: PRBaseViewHolder, position: Int) {
        holder.bindData(mDataList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (mDataList[position]) {
            is PRItemType.Loader -> 1
            is PRItemType.PRItem -> 2
            is PRItemType.Error -> 3
        }
    }

    override fun getItemCount() = mDataList.size

    fun updateDataList(list: List<PRItemType>) {
        Log.d("dilraj", "\n\nupdating list size to ${list.size}\n\n")
        mDataList = list
    }
}
