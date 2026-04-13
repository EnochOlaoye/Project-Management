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
import androidx.compose.ui.platform.LocalContext
import com.example.solutions4u.navigation.NavRoutes
import com.example.solutions4u.network.UserData
import com.example.solutions4u.screens.*
import com.example.solutions4u.ui.theme.Solutions4UTheme
import com.example.solutions4u.ui.theme.ThemeManager
import kotlinx.coroutines.launch

// The main entry point of the app. This is what Android launches first.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Solutions4UTheme {
                Solutions4UApp()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Solutions4UApp() {
    val navController = rememberNavController()

    // This keeps track of the logged-in user across all screens.
    // When null, the user is not logged in.
    var loggedInUser by remember { mutableStateOf<UserData?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Helper to reset theme and log out
    val logoutAndReset: () -> Unit = {
        scope.launch {
            ThemeManager.saveBackgroundColor(context, ThemeManager.DEFAULT_COLOR)
        }
        loggedInUser = null
    }

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
                onDashboardClick = {
                    navController.navigate("profile/1/Guest/guest@email.com")
                },
                onLogoutClick = {
                    logoutAndReset()
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // Swipeable category pages
        composable("categories/{startIndex}") { backStackEntry ->
            val startIndex = backStackEntry.arguments?.getString("startIndex")?.toIntOrNull() ?: 0
            val categories = listOf("Electricity", "Gas", "Car Insurance", "Broadband", "Mobile", "News")
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

        // Register screen - after registering, go back so the user can sign in
        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = { navController.popBackStack() }
            )
        }

        // Profile screen - shows the user's dashboard and chart.
        // The user id, name, and email are passed through the URL.
        composable("profile/{id}/{name}/{email}") { backStackEntry ->
            ProfileScreen(
                userId = backStackEntry.arguments?.getString("id") ?: "",
                userName = backStackEntry.arguments?.getString("name") ?: "",
                userEmail = backStackEntry.arguments?.getString("email") ?: "",
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate("settings/${backStackEntry.arguments?.getString("id")}") }
            )
        }

        composable("settings/{userId}") { backStackEntry ->
            SettingsScreen(
                userId = backStackEntry.arguments?.getString("userId") ?: "",
                userName = loggedInUser?.name ?: "",
                userEmail = loggedInUser?.email ?: "",
                onBackClick = { navController.popBackStack() },
                onAccountDeleted = {
                    logoutAndReset()
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                },
                onLogout = {
                    logoutAndReset()
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}