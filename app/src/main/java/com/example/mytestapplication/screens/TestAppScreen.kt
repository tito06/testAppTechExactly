package com.example.mytestapplication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mytestapplication.helper.AppListState

@Composable
fun TestAppScreen(vm: TestAppVm = hiltViewModel()) {

    val appListState by vm.appListState.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchAppList(378)
    }
    when (appListState) {
        is AppListState.Loading -> {
            CircularProgressIndicator()
        }
        is AppListState.Success -> {
            val appList = (appListState as AppListState.Success).appList
            LazyColumn {
                items(appList) { app ->
                    Text(text = app.app_name)
                }
            }
        }
        is AppListState.Error -> {
            val message = (appListState as AppListState.Error).message
            Text(text = "Error: $message", color = Color.Red)
        }


    }
}

@Preview
@Composable
fun TestAppScreenPreview() {
    TestAppScreen()
}
