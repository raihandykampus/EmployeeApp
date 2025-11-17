package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: String,
    @SerializedName("message") val message: String
)

