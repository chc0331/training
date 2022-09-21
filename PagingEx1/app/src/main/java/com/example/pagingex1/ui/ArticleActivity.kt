package com.example.pagingex1.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagingex1.Injection
import com.example.pagingex1.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ArticleViewModel>(
        factoryProducer = { Injection.provideViewModelFactory(owner = this) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val items = viewModel.items
        val articleAdapter = ArticleAdapter()

        binding.bindAdapter(articleAdapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                /*todo : collect from the paging data flow in the viewmodel
                and submit it to the PagingDataAdapter
                * */
                items.collectLatest {
                    articleAdapter.submitData(it)
                }
            }
        }
    }
}

private fun ActivityMainBinding.bindAdapter(articleAdapter: ArticleAdapter) {
    list.run {
        adapter = articleAdapter
        layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        addItemDecoration(decoration)
    }
}