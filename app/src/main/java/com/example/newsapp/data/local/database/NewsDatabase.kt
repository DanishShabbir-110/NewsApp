package com.example.newsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.database.dao.NewsDao
import com.example.newsapp.data.local.database.entity.SavedNewsEntity

@Database(entities = [SavedNewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}