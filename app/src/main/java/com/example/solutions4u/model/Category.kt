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
        title = "Company Reviews",
        description = "See what customers are saying about energy, broadband, mobile and insurance providers",
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

data class CompanyReview(
    val company: String,
    val category: String,
    val reviewer: String,
    val rating: Int, // 1-5
    val comment: String,
    val date: String
)

val companyReviews = listOf(
    // Electricity
    CompanyReview("Electric Ireland", "Electricity", "John M.", 4, "Reliable service, easy to switch. Customer support was helpful when I had billing issues.", "2026-03-15"),
    CompanyReview("Electric Ireland", "Electricity", "Sarah K.", 3, "Decent enough but a bit pricey compared to others. No issues with supply.", "2026-02-28"),
    CompanyReview("Bord Gáis Energy", "Electricity", "Liam O.", 5, "Switched from Electric Ireland and saving €20 a month. Very happy with the move.", "2026-03-01"),
    CompanyReview("Bord Gáis Energy", "Electricity", "Emma T.", 4, "Good value on the 12 month contract. App could be better though.", "2026-01-20"),
    CompanyReview("SSE Airtricity", "Electricity", "Aoife B.", 5, "Love that it's 100% renewable. Feel good about where my money is going.", "2026-03-10"),
    CompanyReview("SSE Airtricity", "Electricity", "Cian R.", 3, "Green energy is great but the price could be more competitive.", "2026-02-14"),
    CompanyReview("Energia", "Electricity", "Mary F.", 5, "Best fixed rate I could find. No surprises on the bill.", "2026-03-22"),
    CompanyReview("Energia", "Electricity", "Pat D.", 4, "Solid provider, good online portal for tracking usage.", "2026-02-05"),

    // Gas
    CompanyReview("Bord Gáis", "Gas", "Niall C.", 4, "Always reliable, never had an outage. Standard pricing.", "2026-03-18"),
    CompanyReview("Bord Gáis", "Gas", "Claire S.", 3, "Fine service but I think I could get a better deal elsewhere.", "2026-01-30"),
    CompanyReview("Electric Ireland", "Gas", "Tom W.", 5, "Bundled my electricity and gas together, great discount.", "2026-03-05"),
    CompanyReview("Electric Ireland", "Gas", "Sinead L.", 4, "Easy to manage both bills in one place.", "2026-02-19"),
    CompanyReview("Flogas", "Gas", "Brian H.", 5, "Cheapest I could find on a 24 month fix. Locked in before prices went up.", "2026-03-12"),
    CompanyReview("Flogas", "Gas", "Rachel P.", 4, "Good value, straightforward signup process.", "2026-01-25"),

    // Broadband
    CompanyReview("Eir", "Broadband", "David N.", 3, "Speed is okay but customer service took ages to respond.", "2026-03-20"),
    CompanyReview("Eir", "Broadband", "Karen M.", 4, "No contract is handy. Speed has been consistent.", "2026-02-10"),
    CompanyReview("Virgin Media", "Broadband", "Shane O.", 5, "500Mbps is lightning fast. Worth every cent for remote working.", "2026-03-08"),
    CompanyReview("Virgin Media", "Broadband", "Laura B.", 5, "Best broadband I've had. Setup was quick and easy.", "2026-02-22"),
    CompanyReview("Sky", "Broadband", "Mark T.", 4, "Solid speeds and free setup was a nice bonus.", "2026-03-14"),
    CompanyReview("Sky", "Broadband", "Jennifer C.", 3, "Fine broadband but the contract length put me off initially.", "2026-01-18"),
    CompanyReview("Vodafone", "Broadband", "Declan F.", 5, "Great speeds for the price on the 24 month plan.", "2026-03-25"),
    CompanyReview("Vodafone", "Broadband", "Michelle R.", 4, "Happy with the service, no major issues in 6 months.", "2026-02-16"),

    // Mobile
    CompanyReview("Three", "Mobile", "Conor S.", 5, "Unlimited everything for €30 is unbeatable. Network coverage is great.", "2026-03-19"),
    CompanyReview("Three", "Mobile", "Aisling M.", 4, "Really good value. Occasional patchy signal in rural areas.", "2026-02-27"),
    CompanyReview("Vodafone", "Mobile", "Kevin D.", 4, "20GB is plenty and the network is rock solid.", "2026-03-11"),
    CompanyReview("Vodafone", "Mobile", "Orla K.", 3, "Reliable network but pricier than competitors.", "2026-01-29"),
    CompanyReview("Eir Mobile", "Mobile", "Sean B.", 4, "Good budget option, does what it says on the tin.", "2026-03-03"),
    CompanyReview("Eir Mobile", "Mobile", "Fiona L.", 3, "Cheap but the data cap runs out quickly.", "2026-02-08"),
    CompanyReview("48", "Mobile", "Padraig N.", 5, "30GB for under €16 is incredible. No contract either.", "2026-03-16"),
    CompanyReview("48", "Mobile", "Ciara T.", 5, "Best value SIM only plan I've found in Ireland.", "2026-02-20"),

    // Car Insurance
    CompanyReview("AXA", "Car Insurance", "Donal C.", 4, "Comprehensive cover and breakdown assist gave me peace of mind.", "2026-03-21"),
    CompanyReview("AXA", "Car Insurance", "Siobhan W.", 3, "Good cover but renewal quote was higher than expected.", "2026-02-13"),
    CompanyReview("Allianz", "Car Insurance", "Ronan F.", 5, "Windscreen cover came in handy. Claim was processed quickly.", "2026-03-07"),
    CompanyReview("Allianz", "Car Insurance", "Maeve S.", 4, "Competitive pricing and good customer service.", "2026-01-22"),
    CompanyReview("Aviva", "Car Insurance", "Colm D.", 5, "No claims bonus is great. Saved a lot in year two.", "2026-03-13"),
    CompanyReview("Aviva", "Car Insurance", "Deirdre M.", 4, "Easy online claims process. Recommended.", "2026-02-24"),
    CompanyReview("Liberty", "Car Insurance", "Eoin B.", 4, "Good third party cover at a very fair price.", "2026-03-09"),
    CompanyReview("Liberty", "Car Insurance", "Grainne H.", 3, "Basic cover is fine for an older car. Does the job.", "2026-01-31")
)
