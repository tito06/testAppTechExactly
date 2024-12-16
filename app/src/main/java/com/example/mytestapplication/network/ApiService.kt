package com.example.mytestapplication.network

import com.example.mytestapplication.model.AppList
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("list")
    suspend fun getAppList(@Query("kid_id") kid_id: Int):Response<AppList>
}