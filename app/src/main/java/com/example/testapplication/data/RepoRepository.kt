package com.example.testapplication.data

import com.example.testapplication.remote.PRApi
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class RepoRepository @Inject constructor(private val mPRRemoteDataSource: PRApi, @Named("user") private val mUser: String, @Named("repo") private val mRepo: String) {
    suspend fun getClosedPR(): List<PRData>? {
        val list = mPRRemoteDataSource.getClosedPR(mUser, mRepo)
        return mutableListOf<PRData>().apply {
            for (i in 0 .. 50) {
                add(list!![0])
                add(list!![1])
            }
        }
    }
}
