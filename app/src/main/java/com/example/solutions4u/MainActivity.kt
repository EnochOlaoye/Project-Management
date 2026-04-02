package com.example.solutions4u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.solutions4u.navigation.NavRoutes
import com.example.solutions4u.network.UserData
import com.example.solutions4u.screens.*
import com.example.solutions4u.ui.theme.Solutions4UTheme
import com.example.solutions4u.screens.parseHexColor
import com.example.solutions4u.ui.theme.ThemeManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
             // Read the saved colour from DataStore — updates live when user changes it
            val savedColorHex by ThemeManager.getBackgroundColor(this)
                .collectAsState(initial = ThemeManager.DEFAULT_COLOR)
 
            val backgroundColor = remember(savedColorHex) {
                parseHexColor(savedColorHex) ?: Color(0xFF2E7D32)
            }
            Solutions4UTheme(backgroundColor = backgroundColor) {
                Solutions4UApp()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Solutions4UApp(
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val navController = rememberNavController()
    var loggedInUser by remember { mutableStateOf<UserData?>(null) }

    val categories = listOf("Electricity", "Gas", "Car Insurance", "Broadband", "Mobile", "News")

    NavHost(navController = navController, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onCategoryClick = { route ->
                    val index = when (route) {
                        "electricity" -> 0
                        "gas" -> 1
                        "insurance" -> 2
                        "broadband" -> 3
                        "mobile" -> 4
                        "news" -> 5
                        else -> 0
                    }
                    navController.navigate("categories/$index")
                },
                onSignInClick = { navController.navigate(NavRoutes.SIGN_IN) },
                onRegisterClick = { navController.navigate(NavRoutes.REGISTER) },
                loggedInUser = loggedInUser,
                onProfileClick = {
                    val user = loggedInUser
                    if (user != null) {
                        navController.navigate("profile/${user.id}/${user.name}/${user.email}")
                    }
                },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onDashboardClick = {
                    navController.navigate("profile/1/Guest/guest@email.com")
                },
                onLogoutClick = {
                    loggedInUser = null
                }
            )
        }

        // Swipeable category pages
        composable("categories/{startIndex}") { backStackEntry ->
            val startIndex = backStackEntry.arguments?.getString("startIndex")?.toIntOrNull() ?: 0
            val pagerState = rememberPagerState(initialPage = startIndex) { categories.size }

            HorizontalPager(
                state = pagerState
            ) { page ->
                CategoryScreen(
                    categoryName = categories[page],
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(NavRoutes.SIGN_IN) {
            SignInScreen(
                onBackClick = { navController.popBackStack() },
                onSignInSuccess = { user ->
                    loggedInUser = user
                    navController.navigate("profile/${user.id}/${user.name}/${user.email}") {
                        popUpTo(NavRoutes.HOME)
                    }
                }
            )
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = { navController.popBackStack() }
            )
        }
        composable("profile/{id}/{name}/{email}") { backStackEntry ->
        ProfileScreen(
        userId = backStackEntry.arguments?.getString("id") ?: "",
        userName = backStackEntry.arguments?.getString("name") ?: "",
        userEmail = backStackEntry.arguments?.getString("email") ?: "",
        onBackClick = { navController.popBackStack() },
        onSettingsClick = {
            navController.navigate("settings/${backStackEntry.arguments?.getString("id")}")
        }
    )
}

        composable("settings/{userId}") { backStackEntry ->
            SettingsScreen(
            userId = backStackEntry.arguments?.getString("userId") ?: "",
            onBackClick = { navController.popBackStack() },
            onAccountDeleted = {
                loggedInUser = null
                navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        },
            onLogout = {
                loggedInUser = null
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.HOME) { inclusive = true }
                }
            }
    )
}
    }
}