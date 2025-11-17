package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class CreateEmployeeResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: EmployeeCreate
)
