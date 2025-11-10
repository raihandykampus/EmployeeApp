package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class EmployeeResponse (
    @SerializedName(value = "data")
    val data: List<Employee>

)