package com.example.testapplication.remote

import com.example.testapplication.data.PRData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PRApi {
    companion object {
        const val PAGE_SIZE = 10
        const val BASE_URL = "https://api.github.com/"
    }

    @GET("/repos/{user}/{repo}/pulls?state=closed&per_page=11")
    suspend fun getClosedPR(@Path("user") user: String, @Path("repo") repo: String, @Query("page") page: Int): List<PRData>?
}
