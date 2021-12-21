package com.example.myapplication.rxjavaroom.ui

import android.app.VoiceInteractor
import androidx.lifecycle.ViewModel
import com.example.myapplication.rxjavaroom.persistencee.User
import com.example.myapplication.rxjavaroom.persistencee.UserDao
import io.reactivex.Completable
import io.reactivex.Flowable


//View Model for the UserActivity
class UserViewModel(private val dataSource: UserDao) : ViewModel() {

    fun userName(): Flowable<String> {
        return dataSource.getUserById(USER_ID)
            .map { user -> user.userName }
    }

    fun updateUserName(userName: String): Completable {
        val user = User(USER_ID, userName)
        return dataSource.insertUser(user)
    }


    companion object {
        const val USER_ID = "1"
    }
}