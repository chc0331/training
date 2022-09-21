package com.example.pagingex1.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.max

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3_000L

@RequiresApi(Build.VERSION_CODES.O)
private val firstArticleCreatedTime = LocalDateTime.now()

/**
 * A [PagingSource] that loads articles for paging.
 * The [Int] is the paging key or query that is used to fetch more data,
 * and the [Article] specifies that the [PagingSource] fetches an [Article] [List].
 */
class ArticlePagingSource : PagingSource<Int, Article>() {

    /**
     * Loading API for PagingSource.
     * Implement this method to trigger your async load (e.g. from database or network).
     * 실제 데이터를 가져오는 로직 구현을 위한 함수
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        // If params.key is null, it is the first load, so we start loading with STARTING_KEY
        val startKey = params.key ?: STARTING_KEY

        Log.d("heec.choi","load : "+startKey)

        // We fetch as many articles as hinted to by params.loadSize
        val range = startKey.until(startKey + params.loadSize)

        // Simulate a delay for loads adapter the initial load
        if (startKey != STARTING_KEY) delay(LOAD_DELAY_MILLIS)

        return LoadResult.Page(
            data = range.map { number ->
                Article(
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    created = firstArticleCreatedTime.minusDays(number.toLong())
                )
            },
            prevKey = when (startKey) {
                STARTING_KEY -> null
                else -> when (val prevKey = ensureValidKey(key = range.first - params.loadSize)) {
                    //We're at the start, there's nothing more to load
                    STARTING_KEY -> null
                    else -> prevKey
                }
            },
            nextKey = range.last + 1
        )

    }

    /**
     * 초기 key값이나 데이터 로드 중단 후 재 로드시 이전 position에서 중단된 key값을 가져오는 등
     * load에서 사용할 key 값을 가져오는 로직 구현을 위한 함수입니다.
     * */
    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

}