package com.example.flashfeed

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
import com.example.flashfeed.Profile.AccountInfo

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            FlashFeedTheme {
                MainScreen()
            }
        }
    }
}
sealed class Screen(val route: String){
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

    Scaffold(
        floatingActionButton = {
            if (accountInfo != null && currentRoute != Screen.Setup.route) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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
                            containerColor = if (currentRoute == route) activeColor else inactiveColor
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = description,
                                modifier = iconSize,
                                tint = if (currentRoute == route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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
            composable(Screen.Profile.route) {
                accountInfo?.let {
                    Profile(categoryViewModel, newsReelViewModel, it, navController)
                }
            }
        }
    }
}
