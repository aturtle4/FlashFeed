package com.example.flashfeed

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
    var showFirstImage by remember { mutableStateOf(true) }
    val context = LocalContext.current

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
        Crossfade(targetState = showFirstImage, label = "Image Crossfade") { isFirst ->
            if (isFirst) {
                Image(
                    painter = painterResource(id = R.drawable.logo_white),
                    contentDescription = "Image 1",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo_coloured),
                    contentDescription = "Image 2",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



    }
}