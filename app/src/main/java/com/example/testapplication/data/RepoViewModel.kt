package com.example.testapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class RepoViewModel @Inject constructor(private val mRepoRepository: RepoRepository) : ViewModel() {
    private val mPullRequestLiveData = MutableLiveData<ViewState>()
    val pullRequestLiveData by lazy {
        getClosedPullRequests()
        mPullRequestLiveData as LiveData<ViewState>
    }

    private fun getClosedPullRequests() {
        viewModelScope.launch {
            mPullRequestLiveData.value = ViewState.Loading
            val list = mRepoRepository.getClosedPR()
            if (list != null) {
                mPullRequestLiveData.value = ViewState.DataLoaded(list)
            } else {
                mPullRequestLiveData.value = ViewState.Failed
            }
        }
    }
}

sealed class ViewState {
    object Loading : ViewState()
    class DataLoaded(val prList: List<PRData>) : ViewState()
    object Failed : ViewState()
}
