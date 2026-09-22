package com.example.newsapp.presentation.newsdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.newsapp.domain.model.News
import com.example.newsapp.presentation.components.NewsTopAppBar

@Composable
fun NewsDetailScreen(
    news: News,
    uiState: NewsDetailUiState,
    onBackClick: () -> Unit,
    onIntent: (NewsDetailIntent) -> Unit
) {
    LaunchedEffect(news) {
        onIntent(NewsDetailIntent.SetNews(news))
    }

    val currentNews = uiState.news ?: return
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            NewsTopAppBar(
                title = "News Detail",
                showBackButton = true,
                onBackClick = onBackClick,
                showBookmarkButton = true,
                isBookmarked = uiState.isBookMarked,
                onBookmarkClick = {
                    onIntent(NewsDetailIntent.BookmarkClick)
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = currentNews.imageUrl,
                contentDescription = currentNews.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = currentNews.sourceName ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentNews.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (!currentNews.author.isNullOrBlank()) {
                    Text(
                        text = "By ${news.author}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = currentNews.publishedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (!currentNews.description.isNullOrBlank()) {
                    Text(
                        text = currentNews.description,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (!currentNews.content.isNullOrBlank()) {
                    val cleanContent = currentNews.content.substringBefore("[+").trim()

                    val linkStyle = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = buildAnnotatedString {
                            append(cleanContent)
                            append(" ")

                            withLink(
                                LinkAnnotation.Clickable(
                                    tag = "see_more",
                                    styles = TextLinkStyles(
                                        style = linkStyle,
                                        focusedStyle = linkStyle,
                                        hoveredStyle = linkStyle,
                                        pressedStyle = linkStyle
                                    ),
                                    linkInteractionListener = {
                                        uriHandler.openUri(currentNews.newsUrl)
                                    }
                                )
                            ) {
                                append("See more")
                            }
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}