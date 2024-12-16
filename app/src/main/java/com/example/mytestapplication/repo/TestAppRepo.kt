package com.example.mytestapplication.repo

import com.example.mytestapplication.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TestAppRepo @Inject constructor(private  val apiService: ApiService) {

    suspend fun getAppList(kId:Int) = apiService.getAppList(kId)
}