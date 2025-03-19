package com.example.flashfeed.reel_mechanism

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.flashfeed.R
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsReelScreen(newsList: List<NewsArticle>) {
    val pagerState = rememberPagerState(pageCount = { newsList.size })
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { } }
    var isSpeaking by remember { mutableStateOf(false) }
    var displayedWords by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    VerticalPager(state = pagerState) { page ->
        val news = newsList[page]
        val words = news.shortDescription.split(" ") // Split into words
        var isLiked by remember { mutableStateOf(false) } // Track like state

        // Reset displayed words when swiping to a new page
        LaunchedEffect(pagerState.currentPage) {
            val page = pagerState.currentPage // Current page
            val news = newsList[page] // Ensure correct news is fetched

            // Log page changes
            Log.d("NewsReel", "=== PAGE CHANGED TO: $page ===")
            Log.d("NewsReel", "Title: ${news.title}")
            Log.d("NewsReel", "Short Desc: ${news.shortDescription}")

            displayedWords = ""  // Reset displayed words
            tts.stop()
            tts.language = Locale("hi", "IN")

            kotlinx.coroutines.delay(100) // Allow UI to settle

            val words = news.shortDescription.split(" ")
            var currentWordIndex = 0

            Log.d("NewsReel", "Words List Size: ${words.size}")

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    displayedWords = ""
                    Log.d("TTS", "[Page: $page] Started Speaking: ${news.shortDescription}")
                }

                override fun onDone(utteranceId: String?) {
                    isSpeaking = false
                    Log.d("TTS", "[Page: $page] Finished Speaking")
                }

                override fun onError(utteranceId: String?) {
                    Log.e("TTS", "[Page: $page] Error in TTS")
                }

                override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                    if (currentWordIndex < words.size) {
                        displayedWords = words.take(currentWordIndex + 1).joinToString(" ")
                        Log.d("TTS", "[Page: $page] Speaking Word: ${words[currentWordIndex]}")
                        currentWordIndex++
                    }
                }
            })

            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "news_desc_$page"
            tts.speak(news.shortDescription, TextToSpeech.QUEUE_FLUSH, params)
            isSpeaking = true
        }


        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
        ) {
            // Background Image with Blur Effect
            Image(
                painter = rememberAsyncImagePainter(news.imageUrl),
                contentDescription = "News Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(400.dp)
                    .align(Alignment.Center)
                    .blur(10.dp) // Blur effect
                    .background(Color.Black.copy(alpha = 0.5f)) // Dark overlay
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(top = 300.dp, start = 16.dp,end = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedWords,
                    fontSize = 22.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
            // Bottom-left aligned text
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 20.dp)
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "${news.source} • ${news.timestamp}",
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = "# ${news.title}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            // Floating buttons (Like & Share)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp, top = 290.dp)
                    .align(Alignment.CenterEnd),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                // Like Button
                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        painter = painterResource(id = if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline),
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .pointerInput(Unit) {}
                    )
                }

                // Share Button
                IconButton(onClick = { shareNews(context, news.articleLink) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .pointerInput(Unit) {}
                    )
                }
            }


            // Tap to Pause/Resume TTS
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .pointerInput(Unit) {
//                        detectTapGestures(
//                            onTap = {
//                                if (isSpeaking) {
//                                    tts.stop()
//                                    isSpeaking = false
//                                    displayedWords = ""  // Reset
//                                } else {
//                                    displayedWords = ""  // Reset before restarting
//                                    val params = HashMap<String, String>()
//                                    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "news_desc"
//                                    tts.speak(news.shortDescription, TextToSpeech.QUEUE_FLUSH, params)
//                                    isSpeaking = true
//                                }
//                            }
//                        )
//                    }
//            )
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
