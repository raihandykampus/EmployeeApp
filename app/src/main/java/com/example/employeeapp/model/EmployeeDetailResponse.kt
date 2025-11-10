package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class EmployeeDetailResponse(
    @SerializedName(value = "data")
    val data: Employee
)


