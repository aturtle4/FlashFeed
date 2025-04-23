package com.example.flashfeed
// changed
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import com.example.flashfeed.ui.theme.FlashFeedTheme


class StartingPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashFeedTheme {
                CrossfadeImageSwitcher()
            }
        }
    }
}

@Composable
fun CrossfadeImageSwitcher() {
    var currentImageIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val images = listOf(
        R.drawable.starting1,
        R.drawable.starting2,
        R.drawable.starting3,
        R.drawable.starting4
    )

    LaunchedEffect(currentImageIndex) {
        if (currentImageIndex < images.size - 1) {
            kotlinx.coroutines.delay(500) // 1-second delay
            currentImageIndex++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                val intent = Intent(context, LoginPage::class.java)
                context.startActivity(intent)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = currentImageIndex, label = "Image Crossfade") { index ->
            Image(
                painter = painterResource(id = images[index]),
                contentDescription = "Image $index",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}