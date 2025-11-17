package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class EmployeeCreate(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val employeeName: String,
    @SerializedName("salary") val employeeSalary: Int,
    @SerializedName("age") val employeeAge: Int
)

