package com.example.testapplication.remote

import com.example.testapplication.data.PRData
import retrofit2.http.GET
import retrofit2.http.Path

interface PRApi {
    @GET("/repos/{user}/{repo}/pulls?state=closed")
    suspend fun getClosedPR(@Path("user") user: String, @Path("repo") repo: String): List<PRData>?

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}
