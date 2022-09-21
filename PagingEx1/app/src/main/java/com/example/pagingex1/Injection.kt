package com.example.pagingex1

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.pagingex1.data.ArticleRepository
import com.example.pagingex1.ui.ViewModelFactory

object Injection {

    private fun provideArticleRepository(): ArticleRepository = ArticleRepository()

    fun provideViewModelFactory(owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideArticleRepository())
    }
}