package com.example.pagingex1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingex1.data.Article
import com.example.pagingex1.data.ArticleRepository
import kotlinx.coroutines.flow.Flow

private const val ITEMS_PER_PAGE = 5

/**
 * ViewModel for the ArticleActivity screen
 * It works with the ArticleRepository to get the data
 */
class ArticleViewModel(repository: ArticleRepository) : ViewModel() {

    /**
     * Stream of immutable states representative of the UI.
     * Pager는 PagingSource에서 PagingData의 반응형 스트림을 가져오는 메서드를 제공한다.
     * 반응형 스트림을 설정하기 위해 Pager인스턴스를 만들때 PagingConfig와 PagingSource 구현의
     * 인스턴스를 가져 오는 방법을 Pager에 알려주는 함수를 제공해야 한다.
     */
    val items: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.articlePagingSource() }
    ).flow
        /**
         * cachedIn() 연산자는 데이터 스트림을 공유 가능하게 만들고 제공된 CoroutineScope를 사용하여
         * 로드 된 데이터를 캐시한다.
         * */
        .cachedIn(viewModelScope)

    /**
     * Pager 객체는 PagingSource 객체에서 load()메서드를 호출해 LoadParams객체를 제공하고 LoadResult 객체를
     * 반환받는다.
     * */
}