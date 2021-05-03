package com.example.testapplication.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.testapplication.R
import com.example.testapplication.data.PRData
import com.example.testapplication.databinding.PrContainerBinding

class PRAdapter : ListAdapter<PRData, PRViewHolder>(object : DiffUtil.ItemCallback<PRData>() {
    override fun areItemsTheSame(oldItem: PRData, newItem: PRData): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: PRData, newItem: PRData): Boolean {
        return false
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRViewHolder {
        return PRViewHolder(PrContainerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.pr_container, parent, false)))
    }

    override fun onBindViewHolder(holder: PRViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }
}

class PRViewHolder(private val mBinding: PrContainerBinding) : RecyclerView.ViewHolder(mBinding.root) {
    private val mResources = mBinding.root.context.resources

    fun bindData(item: PRData?) {
        if (item != null) {
            Glide.with(mBinding.root.context).clear(mBinding.avatar)
            mBinding.prName.text = item.title
            mBinding.createdAt.text = mResources.getString(R.string.created_at, item.createdAt)
            mBinding.closedAt.text = mResources.getString(R.string.closed_at, item.closedAt)
            mBinding.userName.text = mResources.getString(R.string.by_user, item.userDetails.userName)
            Glide
                .with(mBinding.root.context)
                .load(item.userDetails.avatarUrl)
                .transform(CenterInside(), RoundedCorners(mResources.getDimension(R.dimen.size_6).toInt()))
                .into(mBinding.avatar)
        }
    }
}
