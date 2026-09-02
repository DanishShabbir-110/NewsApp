package com.example.newsapp.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                HomeScreen(
                    onNewsClick = { news ->
                        Toast.makeText(this, news.title, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
