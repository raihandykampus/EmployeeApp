package com.example.employeeapp.network

import com.example.employeeapp.model.CreateEmployeeRequest
import com.example.employeeapp.model.CreateEmployeeResponse
import com.example.employeeapp.model.DeleteResponse
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.model.UpdateEmployeeRequest
import com.example.employeeapp.model.UpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    // handle endpoint base url + /get employees
    @GET("employees")
    fun getAllEmployes(): Call<EmployeeResponse>

    @GET("employee/{id}")
    fun getAllEmployeDetail(
        @Path("id") id: Int
    ): Call<EmployeeDetailResponse>

    @POST("create")
    fun createEmployee(
        @Body body: CreateEmployeeRequest
    ): Call<CreateEmployeeResponse>

    @PUT("update/{id}")
    fun updateEmployee(
        @Path("id") id: Int,
        @Body body: UpdateEmployeeRequest
    ): Call<UpdateResponse>

    @DELETE("delete/{id}")
    fun deleteEmployee(@Path("id") id: Int): Call<DeleteResponse>

}