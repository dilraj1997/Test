package com.example.testapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.data.RepoViewModel
import com.example.testapplication.data.ViewState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mAdapter = PRAdapter()
        mBinding.repoList.addItemDecoration(CustomItemDecoration(resources.getDimension(R.dimen.size_10).toInt()))
        mBinding.repoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.repoList.adapter = mAdapter
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
                    mAdapter.submitList(viewState.prList)
                }
                ViewState.Failed -> {
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.failureText.visibility = View.VISIBLE
                    mBinding.repoList.visibility = View.GONE
                }
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return supportFragmentInjector
    }
}
