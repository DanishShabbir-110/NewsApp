package com.example.newsapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.database.entity.SavedNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(news: SavedNewsEntity)

    @Query("DELETE FROM saved_news WHERE news_url=:newsUrl")
    suspend fun deleteNews(newsUrl: String)

    @Query("SELECT * FROM saved_news")
    fun getSavedNews(): Flow<List<SavedNewsEntity>>

    @Query("SELECT EXISTS(SELECT * FROM saved_news WHERE news_url=:newsUrl)")
    fun isNewsSaved(newsUrl: String): Flow<Boolean>
}