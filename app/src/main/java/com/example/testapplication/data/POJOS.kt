package com.example.testapplication.data

import com.google.gson.annotations.SerializedName

data class PRData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("closed_at")
    val closedAt: String,
    @SerializedName("user")
    val userDetails: UserDetails
) {
    override fun toString() = "$title raised by $userDetails"
}

data class UserDetails(
    @SerializedName("login")
    val userName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
) {
    override fun toString() = "$userName, $avatarUrl"
}
