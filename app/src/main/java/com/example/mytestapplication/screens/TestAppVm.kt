package com.example.mytestapplication.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytestapplication.helper.AppListState
import com.example.mytestapplication.repo.TestAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TestAppVm @Inject constructor(private val testAppRepo: TestAppRepo) : ViewModel() {

        private  val _appListState = MutableStateFlow<AppListState>(AppListState.Loading)
        val appListState: StateFlow<AppListState> = _appListState


    fun fetchAppList(kId :Int){
        viewModelScope.launch {
            try {
                val response = testAppRepo.getAppList(kId)
                if(response.isSuccessful){
                    val appList = response.body()
                    if(appList != null){
                        _appListState.value = AppListState.Success(appList.data.app_list)

                    }else{
                        _appListState.value = AppListState.Error("No data found")

                    }
                }else{
                    _appListState.value = AppListState.Error("Error: ${response.errorBody()?.string()}")

                }
                }catch (e:Exception){
                _appListState.value = AppListState.Error(e.message ?: "Unknown error")

                }

        }
    }
}