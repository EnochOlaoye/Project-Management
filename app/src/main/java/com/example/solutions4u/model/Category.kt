package com.example.solutions4u.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

val categories = listOf(
    Category(
        name = "Electricity",
        description = "Click on the above icon to see where you could save money on your electricity. Pick between several suppliers.",
        icon = Icons.Default.Home,
        route = "electricity"
    ),
    Category(
        name = "Gas",
        description = "Click on the above icon to see where you could save money on your Gas. Pick between several suppliers.",
        icon = Icons.Default.Build,
        route = "gas"
    ),
    Category(
        name = "Car Insurance",
        description = "Click on the above icon to see where you could save money on your Car Insurance. Pick between several suppliers.",
        icon = Icons.Default.ShoppingCart,
        route = "insurance"
    ),
    Category(
        name = "Broadband",
        description = "Click on the above icon to see where you could save money on your internet bill. Pick between several suppliers.",
        icon = Icons.Default.DateRange,
        route = "broadband"
    ),
    Category(
        name = "Mobile",
        description = "Click on the above icon to see where you could save money on your mobile. Pick between several suppliers.",
        icon = Icons.Default.Call,
        route = "mobile"
    ),
    Category(
        name = "News",
        description = "News from the markets and update status on Solutions 4 U.",
        icon = Icons.Default.Notifications,
        route = "news"
    )
)

data class NewsItem(
    val title: String,
    val description: String,
    val category: String
)

val newsItems = listOf(
    NewsItem(
        title = "News - Electrical",
        description = "Weekly news for why electricity market has growing prices",
        category = "Electricity"
    ),
    NewsItem(
        title = "News - Car Insurance",
        description = "Weekly news or insight into car insurance prices in Ireland",
        category = "Insurance"
    ),
    NewsItem(
        title = "News - Solutions 4 U",
        description = "Weekly news letter from our company",
        category = "Company"
    )
)
