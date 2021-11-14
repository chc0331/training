package com.example.myapplication.recyclerview.flowerList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.recyclerview.data.DataSource
import com.example.myapplication.recyclerview.data.Flower
import java.lang.IllegalArgumentException
import kotlin.random.Random

// ViewModel 클래스를 상속하여 정의한 클래스는 개발자가 직접 생성자를 통하여서 인스턴스를 생성할 수 없고,
// ViewModelProvider.Factory 인터페이스를 필요로 합니다.
class FlowersListViewModel(val dataSource: DataSource) : ViewModel() {

    val flowersLiveData = dataSource.getFlowerList()

    fun insertFlower(flowerName: String?, flowerDescription: String?) {
        if (flowerName == null || flowerDescription == null) {
            return
        }

        val image = dataSource.getRandomFlowerImageAsset()
        val newFlower = Flower(
            Random.nextLong(),
            flowerName,
            image,
            flowerDescription
        )

        dataSource.addFlower(newFlower)
    }
}

//ViewModel 클래스를 생성할 때, ViewModel에 초기 파라미터를 전달할 때 사용한다.
class FlowersListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlowersListViewModel::class.java)) {
            return FlowersListViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}