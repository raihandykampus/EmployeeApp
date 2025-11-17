package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class CreateEmployeeRequest(
    @SerializedName("name")
    val employeName: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("salary")
    val salary: Int
)
