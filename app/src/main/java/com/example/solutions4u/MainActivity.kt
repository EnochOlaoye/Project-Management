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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            Solutions4UTheme(darkTheme = isDarkTheme) {
                Solutions4UApp(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@Composable
fun Solutions4UApp(
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val navController = rememberNavController()
    var loggedInUser by remember { mutableStateOf<UserData?>(null) }

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
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onDashboardClick = {
                    navController.navigate("profile/1/Guest/guest@email.com")
                }
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
                onSettingsClick = { navController.navigate("settings") }
            )
        }

        composable("settings") {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}