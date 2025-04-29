package com.example.flashfeed

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flashfeed.Profile.CategoryViewModel
import com.example.flashfeed.Explore.Explore
import com.example.flashfeed.Home.Home
import com.example.flashfeed.Profile.NewsReelViewModel
import com.example.flashfeed.Profile.Profile
import com.example.flashfeed.ui.theme.FlashFeedTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.flashfeed.Misc.AboutTheAppScreen
import com.example.flashfeed.Misc.PrivacyPolicyScreen
import com.example.flashfeed.Misc.TermsAndConditionsScreen
import com.example.flashfeed.Profile.AccountInfo
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get and apply saved language
        val languageCode = LocaleUtils.getLocaleString(this)
        LocaleUtils.setAppLocale(this, languageCode)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            FlashFeedTheme {
                MainScreen()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        // Apply language configuration before attaching base context
        val languageCode = LocaleUtils.getLocaleString(newBase)
        super.attachBaseContext(LocaleUtils.setAppLocale(newBase, languageCode))
    }
}
sealed class Screen(val route: String){
    object PrivacyPolicy : Screen("PrivacyPolicy")
    object TermsAndConditions : Screen("TermsAndConditions")
    object AboutTheApp : Screen("AboutTheApp")
    object Setup : Screen("Setup")
    object Home : Screen("Home")
    object Explore : Screen("Explore")
    object Profile : Screen("Profile")

}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var accountInfo by remember { mutableStateOf<AccountInfo?>(null) }

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.surface

    val context = LocalContext.current
    val categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModel.Factory(context.applicationContext as android.app.Application)
    )
    val newsReelViewModel : NewsReelViewModel = viewModel(
        factory = NewsReelViewModel.Factory(context.applicationContext as android.app.Application)
    )

    val userPreferencesViewModel: com.example.flashfeed.Profile.UserPreferencesViewModel = viewModel(
        factory = com.example.flashfeed.Profile.UserPreferencesViewModel.Factory(context.applicationContext as android.app.Application)
    )

    val userPreferencesState by userPreferencesViewModel.userPreferences.collectAsState(initial = null)

    LaunchedEffect(userPreferencesState) {
        userPreferencesState?.let { prefs ->
            accountInfo = AccountInfo(
                username = prefs.username,
                userIcon = prefs.userImageUri?.toUri() ?: "".toUri(),
                lang = prefs.language
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            if (accountInfo != null && currentRoute != Screen.Setup.route) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start=32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val fabModifier = Modifier.size(60.dp)
                    val iconSize = Modifier.size(32.dp)

                    val fabItems = listOf(
                        Triple(Icons.Default.Home, "Home", Screen.Home.route),
                        Triple(Icons.Default.Search, "Explore", Screen.Explore.route),
                        Triple(Icons.Default.Person, "Profile", Screen.Profile.route)
                    )

                    fabItems.forEach { (icon, description, route) ->
                        FloatingActionButton(
                            onClick = { navController.navigate(route) },
                            shape = CircleShape,
                            modifier = fabModifier,
                            containerColor = if (currentRoute?.contains(route) == true ) activeColor else inactiveColor
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = description,
                                modifier = iconSize,
                                tint = if (currentRoute?.contains(route) == true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (accountInfo == null) Screen.Setup.route else Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.AboutTheApp.route){
                AboutTheAppScreen(navController)
            }
            composable (Screen.PrivacyPolicy.route){
                PrivacyPolicyScreen(navController)
            }
            composable (Screen.TermsAndConditions.route){
                TermsAndConditionsScreen(navController)
            }
            composable(Screen.Setup.route) {
                SetupAccountScreen { info ->
                    accountInfo = info
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Setup.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.Home.route) {accountInfo?.let { Home(categoryViewModel, newsReelViewModel, accountInfo)} }
            composable(Screen.Explore.route) { accountInfo?.let {Explore(categoryViewModel, newsReelViewModel, accountInfo)} }
            composable(
                route = "Profile?openDrawer={openDrawer}",
                arguments = listOf(navArgument("openDrawer") { type = NavType.StringType; defaultValue = "false" })
            ) {
                Profile(
                    viewModel = viewModel(),
                    newsReelViewModel = newsReelViewModel,
                    accountInfo = accountInfo,
                    navController = navController
                )
            }
        }
    }
}
