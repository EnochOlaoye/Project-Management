package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.testTag
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken

// Data class representing a single provider plan
data class Plan(
    val provider: String,
    val planName: String,
    val pricePerMonth: Double,
    val features: String,
    val isCurrent: Boolean = false
)

// Sample plans for each category
fun getSamplePlans(categoryName: String): List<Plan> {
    return when (categoryName) {
        "Electricity" -> listOf(
            Plan("Electric Ireland", "Home Electric+", 85.50, "Standard rate, no contract", isCurrent = true),
            Plan("Bord Gais Energy", "Smart Saver", 72.00, "12 month contract, 15% discount"),
            Plan("SSE Airtricity", "Green Energy", 78.50, "100% renewable, no contract"),
            Plan("Energia", "Fixed Saver", 69.99, "24 month fixed rate")
        )
        "Gas" -> listOf(
            Plan("Bord Gais", "Standard Gas", 62.00, "Standard rate, no contract", isCurrent = true),
            Plan("Electric Ireland", "Gas Saver", 54.00, "12 month contract, 10% discount"),
            Plan("SSE Airtricity", "Gas Plus", 58.50, "No contract, online billing"),
            Plan("Flogas", "Budget Gas", 51.99, "24 month fixed rate")
        )
        "Broadband" -> listOf(
            Plan("Eir", "Fibre Broadband", 45.99, "100Mbps, no contract", isCurrent = true),
            Plan("Virgin Media", "Super Speeds", 39.99, "500Mbps, 12 month contract"),
            Plan("Sky", "Sky Broadband", 42.00, "100Mbps, free setup"),
            Plan("Vodafone", "Home Broadband", 35.99, "200Mbps, 24 month contract")
        )
        "Mobile" -> listOf(
            Plan("Three", "All You Can Eat", 30.00, "Unlimited data, calls, texts", isCurrent = true),
            Plan("Vodafone", "Red Plan", 25.00, "20GB data, unlimited calls"),
            Plan("Eir Mobile", "Value Plan", 20.00, "10GB data, unlimited texts"),
            Plan("48", "Big Plan", 15.99, "30GB data, no contract")
        )
        "Car Insurance" -> listOf(
            Plan("AXA", "Comprehensive", 120.00, "Full cover, breakdown assist", isCurrent = true),
            Plan("Allianz", "Premium Cover", 105.00, "Full cover, windscreen cover"),
            Plan("Aviva", "Complete Cover", 98.50, "Full cover, no claims bonus"),
            Plan("Liberty", "Basic Plus", 89.99, "Third party fire and theft")
        )
        else -> listOf(
            Plan("Provider A", "Basic Plan", 20.00, "Standard package", isCurrent = true),
            Plan("Provider B", "Value Plan", 15.00, "Budget option"),
            Plan("Provider C", "Premium Plan", 25.00, "Full features")
        )
    }
}

// Shows details for a single utility category (like Electricity, Gas, etc).
// Displays the category name and a button to start comparing providers.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryName: String,
    onBackClick: () -> Unit
) {
    // Controls whether the compare view is shown
    var showCompare by remember { mutableStateOf(false) }
    val plans = remember { getSamplePlans(categoryName) }

    // Keeps track of which plans the user has saved for later
    var savedPlans by remember { mutableStateOf<List<Plan>>(emptyList()) }

    // Controls whether the saved plans dialog is shown
    var showSavedDialog by remember { mutableStateOf(false) }

    // Saved plans dialog - shows all plans the user has saved
    if (showSavedDialog) {
        AlertDialog(
            onDismissRequest = { showSavedDialog = false },
            title = {
                Text(
                    text = "Saved Plans",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                if (savedPlans.isEmpty()) {
                    Text("No plans saved yet. Tap the ❤️ on any plan to save it.")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(savedPlans) { plan ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Green600)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = plan.provider,
                                        fontWeight = FontWeight.Bold,
                                        color = White,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = plan.planName,
                                        color = White.copy(alpha = 0.9f),
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "€${"%.2f".format(plan.pricePerMonth)}/month",
                                        fontWeight = FontWeight.Bold,
                                        color = White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                OutlinedButton(onClick = { showSavedDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, color = White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                actions = {
                    // Show saved count badge if any plans are saved
                    if (savedPlans.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .testTag("savedCountBadge")
                        ) {
                            IconButton(onClick = { showSavedDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "View Saved Plans",
                                    tint = White
                                )
                            }
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = savedPlans.size.toString(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background.darken())
            )
        }
    ) { paddingValues ->
        if (showCompare) {
            // Compare view - shows all plans side by side
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .testTag("comparePlansView"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Compare $categoryName Plans",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your current plan is highlighted in green.",
                        fontSize = 14.sp,
                        color = White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Show each plan as a card
                items(plans) { plan ->
                    PlanCard(
                        plan = plan,
                        isSaved = savedPlans.any { it.provider == plan.provider && it.planName == plan.planName },
                        onSaveClick = {
                            // Toggle save - add if not saved, remove if already saved
                            savedPlans = if (savedPlans.any { it.provider == plan.provider && it.planName == plan.planName }) {
                                savedPlans.filter { it.provider != plan.provider || it.planName != plan.planName }
                            } else {
                                savedPlans + plan
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    // View saved plans button
                    if (savedPlans.isNotEmpty()) {
                        Button(
                            onClick = { showSavedDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("viewSavedButton"),
                            colors = ButtonDefaults.buttonColors(containerColor = Red500)
                        ) {
                            Text("View Saved Plans (${savedPlans.size})", color = White)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Button to go back to the category screen
                    OutlinedButton(
                        onClick = { showCompare = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("backToCategoryButton")
                    ) {
                        Text("Back", color = White)
                    }
                }
            }
        } else {
            // Default category view
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = categoryName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Compare $categoryName providers and find the best deals.",
                    fontSize = 16.sp,
                    color = White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showCompare = true },
                    modifier = Modifier.testTag("compareNowButton"),
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) {
                    Text("Compare Now")
                }
            }
        }
    }
}

// A single plan card showing provider, price, features and current plan badge
@Composable
fun PlanCard(
    plan: Plan,
    isSaved: Boolean = false,
    onSaveClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("planCard_${plan.provider}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            // Highlight current plan in green, others in white
            containerColor = if (plan.isCurrent) Green600 else White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan.provider,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (plan.isCurrent) White else Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Show "Current Plan" badge if this is the user's plan
                    if (plan.isCurrent) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Current Plan",
                                fontSize = 11.sp,
                                color = White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    // Save for later button - toggles heart icon
                    IconButton(
                        onClick = onSaveClick,
                        modifier = Modifier.testTag("saveButton_${plan.provider}")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isSaved) "Saved" else "Save for later",
                            tint = if (plan.isCurrent) White else Red500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.planName,
                fontSize = 14.sp,
                color = if (plan.isCurrent) White.copy(alpha = 0.9f) else DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price display
            Text(
                text = "€${"%.2f".format(plan.pricePerMonth)}/month",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (plan.isCurrent) White else Green600
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.features,
                fontSize = 13.sp,
                color = if (plan.isCurrent) White.copy(alpha = 0.8f) else DarkGray
            )
        }
    }
}