package com.example.flashfeed.reel_mechanism

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberAsyncImagePainter
import com.example.flashfeed.Profile.AccountInfo
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.R
import kotlinx.coroutines.delay
import java.util.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission


@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsReelScreen(
    newsList: List<NewsArticle>,
    viewModel: NewsReelViewModel,
    category: String,
    startIndex: Int = 0,
    accountInfo: AccountInfo?
) {
    Log.d("NewsReelScreen", "NewsReelScreen called with category: $category, startIndex: $startIndex")
    val pagerState = rememberPagerState(pageCount = { newsList.size })
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val tts = remember { TextToSpeech(context) { } }
    val language = accountInfo?.lang
    var isSpeaking by remember { mutableStateOf(false) }
    var displayedWords by remember { mutableStateOf("") }
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
        context.registerReceiver(null, filter)
    }

    val batteryPct: Float? = batteryStatus?.let { intent ->
        val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        level * 100 / scale.toFloat()
    }
    val networkType = getNetworkType(context)
    var currentVisiblePage by remember { mutableIntStateOf(startIndex) }
    var isAutoScrolling by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    tts.stop()
                    isSpeaking = false
                    Log.d("TTS", "App moved to background, TTS stopped")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    tts.stop()
                    tts.shutdown()
                    Log.d("TTS", "TTS Shutdown on Destroy")
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            tts.stop()
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.d("TTS", "NewsReelScreen disposed — stopping & shutting down TTS")
        }
    }

    // Autoscroll to previous page on list update
    LaunchedEffect(newsList) {
        isAutoScrolling = true
        pagerState.scrollToPage(currentVisiblePage)
        isAutoScrolling = false
    }

    // Track user scroll and fetch more data if needed
    LaunchedEffect(pagerState.currentPage) {
        currentVisiblePage = pagerState.currentPage
        if (!isAutoScrolling) {
            Log.d("Battery", "Battery percentage: $batteryPct")
            Log.d("Network Type", "Type: $networkType")
            if (((batteryPct != null && batteryPct < 30f ) || (networkType == "cellular")) && newsList.size - currentVisiblePage < 3) {
                Log.d("Pagination", "Not 3")
                viewModel.fetchNews(category, newsList.size + 3, language.toString(), false)
            } else if (((batteryPct != null && batteryPct >= 30f ) && (networkType == "wifi")) && newsList.size - currentVisiblePage < 5) {
                Log.d("Pagination", "Not 10")
                viewModel.fetchNews(category, newsList.size + 10, language.toString(), false)
            } else {
                newsList.size // No need to load more
            }
        }
    }

    VerticalPager(state = pagerState) { page ->
        val news = newsList[page]
        val isLiked = viewModel.isLiked(news.id.toString())

        if (currentVisiblePage == page) {
            LaunchedEffect(currentVisiblePage) {
                displayedWords = ""
                tts.stop()
                tts.language = Locale("hi", "IN")

                delay(100)

                val words = news.content.split(" ")
                var currentWordIndex = 0

                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        displayedWords = ""
                        Log.d("TTS", "[Page: $page] Started Speaking")
                    }

                    override fun onDone(utteranceId: String?) {
                        isSpeaking = false
                        Log.d("TTS", "[Page: $page] Finished Speaking")
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        Log.e("TTS", "[Page: $page] Error in TTS")
                    }

                    override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                        if (currentWordIndex < words.size) {
                            val visibleWords = words.subList(
                                maxOf(0, currentWordIndex - 4),
                                currentWordIndex + 1
                            )
                            displayedWords = visibleWords.joinToString(" ")
                            Log.d("TTS", "[Page: $page] Speaking: ${words[currentWordIndex]}")
                            currentWordIndex++
                        }
                    }
                })

                val params = HashMap<String, String>()
                params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "news_desc_$page"
                tts.speak(news.content, TextToSpeech.QUEUE_FLUSH, params)
                isSpeaking = true
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { viewModel.toggleLike(news.id.toString()) },
                        onLongPress = { openArticleInApp(context, news.articleLink) },
                    )
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter(news.imageUrl),
                contentDescription = "News Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(400.dp)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.background)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(top = 300.dp, start = 16.dp, end = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedWords,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 60.dp)
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "${news.source} • ${news.timestamp}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = news.title,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp, top = 290.dp)
                    .align(Alignment.CenterEnd),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { viewModel.toggleLike(news.id.toString()) }) {
                    Icon(
                        painter = painterResource(id = if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline),
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }

                val isSaved = viewModel.isArticleSaved(news.id.toString())
                Log.d("NewsReelScreen", "isSaved: ${viewModel.getSavedArticles()}")
                IconButton(onClick = {
                    if (isSaved) viewModel.removeSavedArticle(news.id.toString())
                    else viewModel.saveArticle(news)
                }) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) Color.Yellow else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = { shareNews(context, news.articleLink) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

// Function to share the article link
fun shareNews(context: Context, articleLink: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this news: $articleLink")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

fun openArticleInApp(context: Context, articleLink: String) {
    val intent = Intent(context, WebViewActivity::class.java)
    intent.putExtra("url", articleLink)
    context.startActivity(intent)
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun getNetworkType(context: Context): String {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return "none"
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return "none"

    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "cellular"
        else -> "other"
    }
}
