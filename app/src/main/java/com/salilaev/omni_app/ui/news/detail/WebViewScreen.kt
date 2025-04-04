package com.salilaev.omni_app.ui.news.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.viewModels
import com.salilaev.omni_app.ComposeFragment
import com.salilaev.omni_app.data.local.room.entity.NewsEntity
import com.salilaev.omni_app.theme.Primary
import com.salilaev.omni_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewScreen : ComposeFragment() {

    private val viewModel by viewModels<WebViewViewModel>()


    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun Content() {
        val url = arguments?.getString("url") ?: ""
        viewModel.updateUrl(url)

        val isLoading by viewModel.isLoading.collectAsState()

        val savedNews by viewModel.savedNews.collectAsState()
        val isSaved = savedNews.any { it.url == url }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    viewModel.setLoading(true)
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    viewModel.setLoading(false)
                                }
                            }
                            loadUrl(url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            FloatingActionButton(
                onClick = {
                    val newsEntity = NewsEntity(
                        title = arguments?.getString("title") ?: "",
                        description = arguments?.getString("description") ?: "",
                        content = arguments?.getString("content") ?: "",
                        publishedAt = arguments?.getString("publishedAt") ?: "",
                        author = arguments?.getString("author") ?: "",
                        url = arguments?.getString("url") ?: "",
                        urlToImage = arguments?.getString("urlToImage") ?: "",
                        category = arguments?.getString("category") ?: ""
                    )
                    Log.d("WebViewScreen", "NewsEntity: $newsEntity")
                    viewModel.toggleSavedNews(newsEntity)
                },
                containerColor = Primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(36.dp)
                    .size(64.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = if (isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
                    ),
                    contentDescription = if (isSaved) "Saved" else "Save",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }

}