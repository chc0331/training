package com.example.pagingex1.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingex1.data.Article
import com.example.pagingex1.data.createdText
import com.example.pagingex1.databinding.ArticleViewholderBinding

class ArticleAdapter : PagingDataAdapter<Article, ArticleAdapter.ArticleViewHolder>(
    ARTICLE_DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ArticleViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val tile = getItem(position)
        if (tile != null) {
            holder.bind(tile)
        }
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ArticleViewHolder(private val binding: ArticleViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(article: Article) {
            binding.apply {
                title.text = article.title
                description.text = article.description
                created.text = article.createdText
            }
        }
    }

}