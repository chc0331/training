package com.example.myapplication.pictureinpicture

import android.os.SystemClock
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


/*
* Viewmodel and Coroutine 참고 : https://zion830.tistory.com/64
* */
class MainViewModel : ViewModel() {

    private var job: Job? = null

    private var startUptimeMillis = SystemClock.uptimeMillis()
    private val timeMillis = MutableLiveData(0L)

    private val _started = MutableLiveData(false)

    val started: LiveData<Boolean> = _started
    val time = timeMillis.map { millis ->
        val minutes = millis / 1000 / 60
        val m = minutes.toString().padStart(2, '0')
        val seconds = (millis / 1000) % 60
        val s = seconds.toString().padStart(2, '0')
        val hundredths = (millis % 1000) / 10
        val h = hundredths.toString().padStart(2, '0')
        "$m:$s:$h"
    }

    private suspend fun CoroutineScope.start() {
        startUptimeMillis = SystemClock.uptimeMillis() - (timeMillis.value ?: 0L)
        while (isActive) {
            timeMillis.value = SystemClock.uptimeMillis() - startUptimeMillis
            //updates on every render frame.
            awaitFrame()
        }
    }

    /*
    * Starts the stopwatch if it is not yet started, or pauses it if it is already started.
    * */
    fun startOrPause() {
        if (_started.value == true) {
            _started.value = false
            job?.cancel()
        } else {
            _started.value = true
            job = viewModelScope.launch { start() }
        }
    }

    /*
    * Clears the stopwatch to 00:00:00
    * */
    fun clear() {
        startUptimeMillis = SystemClock.uptimeMillis()
        timeMillis.value = 0L
    }
}