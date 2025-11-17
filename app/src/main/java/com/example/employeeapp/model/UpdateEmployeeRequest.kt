package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class UpdateEmployeeRequest(
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int,
    @SerializedName("salary") val salary: Int
)

