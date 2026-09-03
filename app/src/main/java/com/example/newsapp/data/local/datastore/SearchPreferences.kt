package com.example.newsapp.data.local.datastore

import android.content.SharedPreferences
import com.example.newsapp.utils.Constants.KEY_RECENT_SEARCHES
import com.example.newsapp.utils.Constants.MAX_RECENT_SEARCHES
import com.example.newsapp.utils.Constants.SEPARATOR


class SearchPreferences(private val sharedPreferences: SharedPreferences) {

    fun getRecentSearches(): List<String> {
        val searches = sharedPreferences.getString(KEY_RECENT_SEARCHES, null)

        if (searches.isNullOrEmpty()) return emptyList()
        return searches.split(SEPARATOR)
    }

    fun saveRecentSearcher(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return
        val currentSearches = getRecentSearches()

        val updateSearches = listOf(cleanQuery) + currentSearches.filterNot {
            it.equals(
                cleanQuery,
                ignoreCase = true
            )
        }

        sharedPreferences.edit().putString(
            KEY_RECENT_SEARCHES,
            updateSearches.take(MAX_RECENT_SEARCHES).joinToString(SEPARATOR)
        ).apply()
    }

    fun clearRecentSearches() {
        sharedPreferences.edit()
            .remove(KEY_RECENT_SEARCHES)
            .apply()
    }

}