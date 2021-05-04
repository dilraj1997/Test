package com.example.testapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.ui.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoViewModel @Inject constructor(private val mRepoRepository: RepoRepository) : ViewModel() {
    private val mPullRequestLiveData = MutableLiveData<ViewState>()
    val pullRequestLiveData by lazy {
        getClosedPullRequests()
        mPullRequestLiveData as LiveData<ViewState>
    }

    private val mMorePullRequestLiveData = MutableLiveData<PagedData>()
    val morePullRequestLiveData by lazy {
        mMorePullRequestLiveData as LiveData<PagedData>
    }

    private var mCursor = 1
    private var mIsLoading = false
    private var mIsLastPageFetched = false
    private var mHasErrorOccurred = false

    fun getClosedPullRequests() {
        viewModelScope.launch {
            mPullRequestLiveData.value = ViewState.Loading
            val list = mRepoRepository.getClosedPR(mCursor)
            if (list != null) {
                mPullRequestLiveData.value = ViewState.DataLoaded(list)
            } else {
                mPullRequestLiveData.value = ViewState.Failed
            }
        }
    }

    fun loadMoreItems(isForce: Boolean = false) {
        if (!mIsLoading && !mIsLastPageFetched && (isForce || (!isForce && !mHasErrorOccurred))) {
            mIsLoading = true
            viewModelScope.launch {
                if (!isForce) {
                    mCursor += 1
                }
                mRepoRepository.getPaginatedClosedPR(mCursor, isForce).collect { pagedData ->
                    when (pagedData.pageValidity) {
                        true -> {
                            mIsLastPageFetched = true
                        }
                        null -> {
                            mHasErrorOccurred = true
                        }
                        false -> {
                            mHasErrorOccurred = false
                        }
                    }
                    mMorePullRequestLiveData.value = pagedData
                }
                mIsLoading = false
            }
        }
    }
}

