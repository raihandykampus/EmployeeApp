package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class UpdateResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val updatedData: UpdatedData?,
    @SerializedName("message") val message: String
)

