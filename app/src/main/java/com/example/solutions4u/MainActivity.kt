package com.example.solutions4u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.solutions4u.navigation.NavRoutes
import com.example.solutions4u.screens.*
import com.example.solutions4u.ui.theme.Solutions4UTheme

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

@Composable
fun Solutions4UApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.HOME) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onCategoryClick = { route -> navController.navigate(route) },
                onSignInClick = { navController.navigate(NavRoutes.SIGN_IN) },
                onRegisterClick = { navController.navigate(NavRoutes.REGISTER) }
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
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}

