package com.example.flashfeed.Profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.flashfeed.R
import com.example.flashfeed.reel_mechanism.NewsArticle
import com.example.flashfeed.reel_mechanism.NewsReelScreen
import setAppLocale
import androidx.core.content.edit
import com.example.flashfeed.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    viewModel: CategoryViewModel,
    newsReelViewModel: NewsReelViewModel,
    accountInfo: AccountInfo?,
    navController: NavHostController
) {
    var drawerOpen by remember { mutableStateOf(false) }

    val currentRoute = navController.currentBackStackEntry?.destination?.route

// Log the current route
    Log.d("NavController", "Current Route: $currentRoute")
    var openSavedNews by remember { mutableStateOf(false) }
    var selectedSavedNewsIndex by remember { mutableIntStateOf(0) }


    val news by newsReelViewModel.savedNewsFlow.collectAsState()

    if (openSavedNews) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // NewsReelScreen as background
            NewsReelScreen(newsList = news, newsReelViewModel, "TRENDING", startIndex = selectedSavedNewsIndex, accountInfo = accountInfo)

            // Floating Action Button as back
            FloatingActionButton(
                onClick = { openSavedNews = false },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    } else if (drawerOpen) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    IconButton(onClick = { drawerOpen = false }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                TextButton(onClick = { navController.navigate("Setup") }) {
                    Text(text = stringResource(id = R.string.update_profile), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                }
                CategorySelector(viewModel = viewModel)
                LangSelector(accountInfo = accountInfo)
                TextButton(onClick = { navController.navigate("PrivacyPolicy") }) {
                    Text(text = stringResource(id = R.string.privacy_policy), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = { navController.navigate("TermsAndConditions") }) {
                    Text(text = stringResource(id = R.string.terms_and_conditions), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = { navController.navigate("AboutTheApp") }) {
                    Text(text = stringResource(id = R.string.about_the_app), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                ProfileHeader(accountInfo = accountInfo)
                Spacer(modifier = Modifier.height(55.dp))

                SavedArticles(news) {index ->
                    Log.d("news-saved-articles","$index")
                    selectedSavedNewsIndex = index
                    openSavedNews = true
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = { drawerOpen = !drawerOpen }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = "Settings-Menu"
                    )
                }
            }
        }
    }

}

@Composable
fun ProfileHeader(
    accountInfo: AccountInfo?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("image_null","${accountInfo?.userIcon}")
                if (accountInfo?.userIcon != null) {
                    // If a user image is set, show it
                    Image(
                        painter = rememberAsyncImagePainter(model = accountInfo.userIcon),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(175.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                } else {
                    // Otherwise show default icon
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Default Profile Icon",
                        modifier = Modifier.size(175.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                Text(
                    text = accountInfo?.username ?: "User", // <-- use username from AccountInfo
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedArticles(articles: List<NewsArticle>, onArticleClick: (Int) -> Unit) {
    Log.d("news-saved-articles","$articles")
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Saved Articles",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(articles) { index, article ->
                    ArticleCard(article) {
                        onArticleClick(index)
                    }
                }
            }
        }
    }
}


@Composable
fun ArticleCard(article: NewsArticle, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(0.75f)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 300f
                    )
                )
        )

        // Title text at the bottom
        Text(
            text = article.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}

@Composable
fun LangSelector(accountInfo: AccountInfo?) {
    var isExpanded by remember { mutableStateOf(false) }
    val languages = mapOf("English" to "en", "Hindi" to "hi", "Bengali" to "bn", "Urdu" to "ur")
    val context = LocalContext.current

    // Get current language from LocaleUtils
    val currentLocale = LocaleUtils.getLocaleString(context)

    var selectedLanguage by remember {
        mutableStateOf(languages.entries.find { it.value == currentLocale }?.key ?: "English")
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.select_language),
                style = MaterialTheme.typography.titleSmall,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        if (isExpanded) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(languages.keys.toList()) { language ->
                    val isSelected = selectedLanguage == language

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            .clickable(enabled = !isSelected) {
                                if (!isSelected) {
                                    selectedLanguage = language
                                    val langCode = languages[language] ?: "en"

                                    // Update accountInfo
                                    accountInfo?.lang = langCode

                                    // Save language preference
                                    LocaleUtils.setAppLocale(context, langCode)

                                    // Restart the app
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    context.startActivity(intent)
                                    (context as? Activity)?.finish()
                                }
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = language,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = language,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySelector(viewModel: CategoryViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    val categorySelection = viewModel.categorySelection
    val selectedCount = categorySelection.values.count { it }
    val context = LocalContext.current
    val languageMap = {
        mapOf(
            "National" to context.getString(R.string.national),
            "Business" to context.getString(R.string.business),
            "Sports" to context.getString(R.string.sports),
            "World" to context.getString(R.string.world),
            "Politics" to context.getString(R.string.politics),
            "Technology" to context.getString(R.string.technology),
            "Startup" to context.getString(R.string.startup),
            "Entrepreneurship" to context.getString(R.string.entrepreneurship),
            "Miscellaneous" to context.getString(R.string.miscellaneous),
            "Science" to context.getString(R.string.science),
            "Automobile" to context.getString(R.string.automobile)
        )
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(R.string.categories)} ($selectedCount/5)",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        // Collapsible content
        if (isExpanded) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(viewModel.getAllCategories()) { category ->
                    val isSelected = categorySelection[category.name] == true
                    val isTrending = category.name == "Trending"
                    val isEnabled = isTrending || isSelected || selectedCount < 5

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            .clickable(enabled = isEnabled && !isTrending) {
                                if (!isTrending) viewModel.toggleCategory(category.name)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = languageMap()[category.name] ?: category.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }
            }
            val context = LocalContext.current

            if (selectedCount == 5) {
                Toast.makeText(context, "Limit reached", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
