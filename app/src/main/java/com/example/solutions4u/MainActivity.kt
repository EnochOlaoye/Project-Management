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
import com.example.solutions4u.network.PropertyRepository
import com.example.solutions4u.network.PropertyResult

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
    var activePropertyIcon by remember { mutableStateOf("home") }
    
    NavHost(navController = navController, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onCategoryClick = { route -> navController.navigate(route) },
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
                },

                onFaqClick = { navController.navigate(NavRoutes.FAQ) },
                onContactClick = { navController.navigate(NavRoutes.CONTACT) },
                onAboutClick = { navController.navigate(NavRoutes.ABOUT) },
                activePropertyIcon = activePropertyIcon,
                onSearchClick = { navController.navigate(NavRoutes.SEARCH) }
            )
        }
        composable(NavRoutes.ELECTRICITY) {
            CategoryScreen(categoryName = "Electricity", onBackClick = { navController.popBackStack() })
        }
        composable(NavRoutes.GAS) {
            CategoryScreen(categoryName = "Gas", onBackClick = { navController.popBackStack() })
        }
        composable(NavRoutes.INSURANCE) {
            CategoryScreen(categoryName = "Car Insurance", onBackClick = { navController.popBackStack() })
        }
        composable(NavRoutes.BROADBAND) {
            CategoryScreen(categoryName = "Broadband", onBackClick = { navController.popBackStack() })
        }
        composable(NavRoutes.MOBILE) {
            CategoryScreen(categoryName = "Mobile", onBackClick = { navController.popBackStack() })
        }
        composable(NavRoutes.NEWS) {
            CategoryScreen(categoryName = "News", onBackClick = { navController.popBackStack() })
        }

        composable(NavRoutes.SIGN_IN) {
            SignInScreen(
                onBackClick = { navController.popBackStack() },
                onSignInSuccess = { user ->
                    loggedInUser = user
                    scope.launch {
        val savedColor = ThemeManager.getUserColor(context, user.id)
        ThemeManager.saveBackgroundColor(context, savedColor)
        
        // Load active property icon
        val propertyRepository = PropertyRepository()
        when (val result = propertyRepository.getProperties(user.id)) {
            is PropertyResult.Success -> {
                activePropertyIcon = result.properties.firstOrNull()?.icon ?: "home"
            }
            else -> {}
        }
    }
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
                },
                onIconChanged = { iconKey -> activePropertyIcon = iconKey }
            )
        }

        composable(NavRoutes.FAQ) {
            FaqScreen(onBackClick = { navController.popBackStack() },
            isAdmin = loggedInUser?.email == "admin@solution4u.ie"
            )
        }

        composable(NavRoutes.CONTACT) {
            ContactScreen(onBackClick = { navController.popBackStack() })
        }

        composable(NavRoutes.ABOUT) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }

        composable(NavRoutes.SEARCH) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onCompanyClick = { route -> navController.navigate(route) }
            )
        }
    }
}