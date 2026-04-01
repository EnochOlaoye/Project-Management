package com.example.solutions4u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.solutions4u.navigation.NavRoutes
import com.example.solutions4u.network.UserData
import com.example.solutions4u.screens.*
import com.example.solutions4u.ui.theme.Solutions4UTheme

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

// The root composable that sets up all the navigation for the app.
// It keeps track of whether a user is logged in and passes that info to the screens that need it.
@Composable
fun Solutions4UApp() {
    val navController = rememberNavController()

    // This keeps track of the logged-in user across all screens.
    // When null, the user is not logged in.
    var loggedInUser by remember { mutableStateOf<UserData?>(null) }

    NavHost(navController = navController, startDestination = NavRoutes.HOME) {

        // Home screen - the main landing page with categories, news, and login buttons
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
                }
            )
        }

        // Each utility category gets its own screen
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

        // Sign in screen - on success, save the user and go to their profile
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
            navController.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.HOME) { inclusive = true }
            }
        }
    )
}
    }
}
