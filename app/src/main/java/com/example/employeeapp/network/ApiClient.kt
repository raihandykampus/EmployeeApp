package com.example.employeeapp.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun getInstance(): ApiService {

        // digunakan untuk logging
        // debugging melihat apa yang diterima response dari server
        val mHttpLoggingInterceptor = HttpLoggingInterceptor{ msg -> Log.d("HTTP", msg)}
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        val builder = Retrofit.Builder()
            .baseUrl("https://dummy.restapiexample.com/api/v1/")
            // Gson digunakan untuk mapping dari json ke object kotlin
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        return builder.create(ApiService::class.java)
    }
}