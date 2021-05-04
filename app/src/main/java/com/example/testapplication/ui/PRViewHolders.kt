package com.example.testapplication.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.testapplication.R
import com.example.testapplication.databinding.ErrorContainerBinding
import com.example.testapplication.databinding.LoadingContainerBinding
import com.example.testapplication.databinding.PrContainerBinding

class PRViewHolder(private val mBinding: PrContainerBinding) : PRBaseViewHolder(mBinding.root) {
    private val mResources = mBinding.root.context.resources

    override fun bindData(item: PRItemType) {
        if (item is PRItemType.PRItem) {
            Glide.with(mBinding.root.context).clear(mBinding.avatar)
            mBinding.prName.text = item.prData.title
            mBinding.createdAt.text = mResources.getString(R.string.created_at, item.prData.createdAt)
            mBinding.closedAt.text = mResources.getString(R.string.closed_at, item.prData.closedAt)
            mBinding.userName.text = mResources.getString(R.string.by_user, item.prData.userDetails.userName)
            Glide.with(mBinding.root.context)
                .load(item.prData.userDetails.avatarUrl)
                .transform(
                    CenterInside(),
                    RoundedCorners(mResources.getDimension(R.dimen.size_6).toInt())
                )
                .into(mBinding.avatar)
        }
    }
}

class PRLoadingHolder(private val mBinding: LoadingContainerBinding) : PRBaseViewHolder(mBinding.root) {
    override fun bindData(PRItem: PRItemType) {

    }
}

class PRErrorHolder(private val mBinding: ErrorContainerBinding, private val mErrorClickListener: () -> Unit) : PRBaseViewHolder(mBinding.root) {
    init {
        mBinding.root.setOnClickListener {
            mErrorClickListener()
        }
    }

    override fun bindData(PRItem: PRItemType) {

    }
}

abstract class PRBaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindData(PRItem: PRItemType)
}