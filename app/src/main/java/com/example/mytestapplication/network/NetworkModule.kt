package com.example.mytestapplication.network

import androidx.compose.ui.tooling.preview.Preview
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun  provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://navkiraninfotech.com/g-mee-api/api/v1/apps/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService{
        return retrofit.create(ApiService::class.java)

    }



}