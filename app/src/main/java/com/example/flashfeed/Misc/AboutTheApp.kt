package com.example.flashfeed.Misc

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutTheAppScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About The App") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "FlashFeed",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "FlashFeed brings you real-time news updates in a simple and fast reels-style interface. Stay informed, stay ahead!",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
