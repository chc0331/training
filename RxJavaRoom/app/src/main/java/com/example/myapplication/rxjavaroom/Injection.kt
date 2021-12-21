package com.example.myapplication.rxjavaroom

import android.content.Context
import com.example.myapplication.rxjavaroom.persistencee.UserDao
import com.example.myapplication.rxjavaroom.persistencee.UsersDatabase
import com.example.myapplication.rxjavaroom.ui.ViewModelFactory

/*
* Enables injection of data sources.
* */
object Injection {

    fun provideUserDataSource(context: Context): UserDao {
        val database = UsersDatabase.getInstance(context)
        return database.userDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }
}