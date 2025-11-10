package com.example.employeeapp.model

import com.google.gson.annotations.SerializedName

data class Employee (
    @SerializedName(value = "id")
    val id: Int,

    @SerializedName(value = "employee_name")
    val employeeName: String,

    @SerializedName(value = "employee_salary")
    val employeeSalary: Int,

    @SerializedName(value = "employee_age")
    val age: Int
)