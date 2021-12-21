package com.example.myapplication.rxjavaroom.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.rxjavaroom.persistencee.UserDao
import java.lang.IllegalArgumentException

//viewModel에 원하는 파라미터를 넣어 생성하는 방법을 제공한다.
class ViewModelFactory(private val dataSource: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}