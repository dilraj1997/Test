package com.example.testapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoViewModel(private val mRepoRepository: RepoRepository) : ViewModel() {
    private val mPullRequestLiveData = MutableLiveData<List<String>>()

    fun getClosedPullRequests() {
        viewModelScope.launch {

        }
    }
}
