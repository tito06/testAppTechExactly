package com.example.mytestapplication.helper

import com.example.mytestapplication.model.App

sealed class AppListState {

    object  Loading: AppListState()
    data class Success(val appList: List<App>): AppListState()
    data class Error(val message: String): AppListState()
}