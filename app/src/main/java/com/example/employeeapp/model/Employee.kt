package com.example.employeeapp.model

import android.R
import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("id")
    val id: Int,

    @SerializedName("employee_name")
    val employeeName: String,

    @SerializedName("employee_salary")
    val employeeSalary: Int,

    @SerializedName("employee_age")
    val employeeAge: Int,

    @SerializedName("profile_image")
    val profilImage: String = ""
)
