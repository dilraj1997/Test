package com.example.testapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.data.OperationType
import com.example.testapplication.data.RepoViewModel
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.di.CustomViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class RepoActivity : AppCompatActivity(), HasAndroidInjector {
    companion object {
        const val TAG = "RepoActivity"
    }

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: CustomViewModelFactory

    private val mRepoViewModel by viewModels<RepoViewModel> {
        viewModelFactory
    }

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: PRAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mAdapter = PRAdapter(errorClickListener = {
            mRepoViewModel.loadMoreItems(isForce = true)
        })
        mAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mBinding.repoList.addItemDecoration(CustomItemDecoration(resources.getDimension(R.dimen.size_10).toInt()))
        mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.repoList.layoutManager = mLinearLayoutManager
        mBinding.repoList.adapter = mAdapter

        mBinding.repoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = mLinearLayoutManager.childCount
                val totalItemCount = mLinearLayoutManager.itemCount
                val firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= 10) {
                    mRepoViewModel.loadMoreItems()
                }
            }
        })
        mBinding.failureText.setOnClickListener {
            mRepoViewModel.getClosedPullRequests()
        }
        startObserving()
    }

    private fun startObserving() {
        mRepoViewModel.pullRequestLiveData.observe(this) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.failureText.visibility = View.GONE
                    mBinding.repoList.visibility = View.GONE
                }
                is ViewState.DataLoaded -> {
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.repoList.visibility = View.GONE
                    mBinding.repoList.visibility = View.VISIBLE
                    mAdapter.updateDataList(viewState.prList)
                    mAdapter.notifyItemRangeInserted(0, viewState.prList.lastIndex)
                }
                ViewState.Failed -> {
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.failureText.visibility = View.VISIBLE
                    mBinding.repoList.visibility = View.GONE
                }
            }
        }

        mRepoViewModel.morePullRequestLiveData.observe(this) { pagedData ->
            mAdapter.updateDataList(pagedData.dataList)
            Log.d("dilraj", "${pagedData.operationType}, ${pagedData.index}, ${pagedData.dataList}")
            when (pagedData.operationType) {
                OperationType.ADDED -> {
                    mAdapter.notifyItemRemoved(pagedData.index)
                    mAdapter.notifyItemRangeInserted(pagedData.index, pagedData.dataList.lastIndex)
                }
                OperationType.CHANGED -> {
                    mAdapter.notifyItemChanged(pagedData.index)
                }
                OperationType.REMOVED -> {
                    mAdapter.notifyItemRemoved(pagedData.index)
                }
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return supportFragmentInjector
    }
}
