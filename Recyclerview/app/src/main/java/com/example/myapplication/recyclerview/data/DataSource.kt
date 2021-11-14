package com.example.myapplication.recyclerview.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/* Handles operations on flowersLiveData and holds details about it. */
class DataSource(resources: Resources) {
    private val initialFlowerList = flowerList(resources)
    private val flowersLiveData = MutableLiveData(initialFlowerList)

    /* Adds flower to liveData and posts value. */
    fun addFlower(flower: Flower) {
        val currentList = flowersLiveData.value
        if (currentList == null) {
            flowersLiveData.postValue(listOf(flower))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, flower)
            flowersLiveData.postValue(updatedList)
        }
    }

    /*Removes flower from liveData and posts value.*/
    fun removeFlower(flower: Flower) {
        val currentList = flowersLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(flower)
            flowersLiveData.postValue(updatedList)
        }
    }

    /*Returns flower given an Id.*/
    fun getFlowerForId(id: Long): Flower? {
        flowersLiveData.value?.let { flowers ->
            return flowers.firstOrNull { it.id == id }
        }
        return null
    }

    fun getFlowerList(): LiveData<List<Flower>> {
        return flowersLiveData
    }

    /*Returns a random flower asset for flowers that are added.*/
    fun getRandomFlowerImageAsset(): Int? {
        val randomNumber = (initialFlowerList.indices).random()
        return initialFlowerList[randomNumber].image
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }

}