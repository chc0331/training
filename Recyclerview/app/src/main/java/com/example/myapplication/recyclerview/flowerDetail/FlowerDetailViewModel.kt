package com.example.myapplication.recyclerview.flowerDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.recyclerview.data.DataSource
import com.example.myapplication.recyclerview.data.Flower
import java.lang.IllegalArgumentException

class FlowerDetailViewModel(private val datasource: DataSource) : ViewModel() {
    fun getFlowerForId(id: Long): Flower? {
        return datasource.getFlowerForId(id)
    }

    fun removeFlower(flower: Flower) {
        datasource.removeFlower(flower)
    }
}

class FlowerDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlowerDetailViewModel::class.java)) {
            return FlowerDetailViewModel(
                datasource =
                DataSource.getDataSource
                    (context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }

}