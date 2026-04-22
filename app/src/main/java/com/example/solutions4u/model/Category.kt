package com.example.solutions4u.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

// Represents a utility category like Electricity or Gas that the user can browse
data class Category(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

// The list of all utility categories available in the app
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

// Represents a single utility bill that the user adds to their profile dashboard
data class UtilityBill(
    val id: Int,
    val category: String,
    val provider: String,
    val amount: Double,
    val date: String
)

// Represents a news article shown on the home screen
data class NewsItem(
    val title: String,
    val description: String,
    val category: String
)

// Sample news items displayed on the home screen
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

data class SearchableCompany(
    val name: String,
    val category: String,
    val route: String,
    val aliases: List<String> = emptyList()
)

val searchableCompanies = listOf(
    SearchableCompany("Electric Ireland", "Electricity", "electricity", listOf("electric", "ireland")),
    SearchableCompany("Bord Gáis Energy", "Electricity", "electricity", listOf("bord gais", "bordgais", "bord gas")),
    SearchableCompany("SSE Airtricity", "Electricity", "electricity", listOf("sse", "airtricity")),
    SearchableCompany("Energia", "Electricity", "electricity", listOf("energia")),
    SearchableCompany("Bord Gáis", "Gas", "gas", listOf("bord gais", "bordgais", "bord gas")),
    SearchableCompany("Electric Ireland", "Gas", "gas", listOf("electric ireland")),
    SearchableCompany("SSE Airtricity", "Gas", "gas", listOf("sse", "airtricity")),
    SearchableCompany("Flogas", "Gas", "gas", listOf("flo gas", "flogas")),
    SearchableCompany("Eir", "Broadband", "broadband", listOf("eircom", "eir")),
    SearchableCompany("Virgin Media", "Broadband", "broadband", listOf("virgin", "media")),
    SearchableCompany("Sky", "Broadband", "broadband", listOf("sky broadband")),
    SearchableCompany("Vodafone", "Broadband", "broadband", listOf("vodafone broadband")),
    SearchableCompany("Three", "Mobile", "mobile", listOf("three mobile", "3")),
    SearchableCompany("Vodafone", "Mobile", "mobile", listOf("vodafone mobile")),
    SearchableCompany("Eir Mobile", "Mobile", "mobile", listOf("eir mobile")),
    SearchableCompany("48", "Mobile", "mobile", listOf("fortyeight", "48 mobile")),
    SearchableCompany("AXA", "Car Insurance", "insurance", listOf("axa insurance")),
    SearchableCompany("Allianz", "Car Insurance", "insurance", listOf("allianz insurance")),
    SearchableCompany("Aviva", "Car Insurance", "insurance", listOf("aviva insurance")),
    SearchableCompany("Liberty", "Car Insurance", "insurance", listOf("liberty insurance"))
)

fun searchCompanies(query: String): List<SearchableCompany> {
    if (query.isBlank()) return emptyList()
    val normalised = query.lowercase()
        .replace("á", "a").replace("é", "e")
        .replace("í", "i").replace("ó", "o")
        .replace("ú", "u")
    return searchableCompanies.filter { company ->
        val companyNormalised = company.name.lowercase()
            .replace("á", "a").replace("é", "e")
            .replace("í", "i").replace("ó", "o")
            .replace("ú", "u")
        val aliasMatch = company.aliases.any { alias ->
            alias.replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u")
                .contains(normalised)
        }
        companyNormalised.contains(normalised) || aliasMatch
    }
}
