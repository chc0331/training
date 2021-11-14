package com.example.myapplication.recyclerview.flowerList

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.recyclerview.R
import com.example.myapplication.recyclerview.addFlower.AddFlowerActivity
import com.example.myapplication.recyclerview.addFlower.FLOWER_DESCRIPTION
import com.example.myapplication.recyclerview.addFlower.FLOWER_NAME
import com.example.myapplication.recyclerview.data.Flower
import com.example.myapplication.recyclerview.flowerDetail.FlowerDetailActivity

const val FLOWER_ID = "flower id"

class FlowerListActivity : AppCompatActivity() {
    private val newFlowerActivityRequestCode = 1
    private val flowersListViewModel by viewModels<FlowersListViewModel> {
        FlowersListViewModelFactory(this)
    }
    /*파라미터, 멤버 변수, 함수를 갖고있지 않은 심플한 ViewModel 일때, 두 가지 방법
    private val viewmodel : FlowersListViewModel = ViewModelProvider(this).get()

    private val viewmodel: FlowersListViewModel =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FlowersListViewModel::class.java)*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerAdapter = HeaderAdapter()
        val flowersAdapter = FlowersAdapter { flower ->
            adapterOnClick(flower)
        }
        val concatAdapter = ConcatAdapter(headerAdapter, flowersAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = concatAdapter

        flowersListViewModel.flowersLiveData.observe(this, {
            it?.let {
                flowersAdapter.submitList(it as MutableList<Flower>)
                headerAdapter.updateFlowerCount(it.size)
            }
        })

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabOnClick()
        }
    }

    private fun adapterOnClick(flower: Flower) {
        val intent = Intent(this, FlowerDetailActivity()::class.java)
        intent.putExtra(FLOWER_ID, flower.id)
        startActivity(intent)
    }

    private fun fabOnClick() {
        val intent = Intent(this, AddFlowerActivity::class.java)
        startActivityForResult(intent, newFlowerActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newFlowerActivityRequestCode &&
            resultCode == Activity.RESULT_OK
        ) {
            data?.let { data ->
                val flowerName = data.getStringExtra(FLOWER_NAME)
                val flowerDescription = data.getStringExtra(FLOWER_DESCRIPTION)

                flowersListViewModel.insertFlower(flowerName, flowerDescription)
            }
        }
    }
}