package com.example.flashfeed.Profile

import android.net.Uri
import androidx.compose.ui.graphics.vector.ImageVector

data class AccountInfo(
    val username: String,
    val userIcon: Uri?,
    var lang: String,
)
