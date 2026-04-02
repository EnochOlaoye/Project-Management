package com.example.solutions4u.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.model.UtilityBill
import com.example.solutions4u.ui.theme.*

// The user's profile screen focused on two things:
// 1. A bar chart showing spending broken down by category
// 2. The ability to add new utility bills via a floating + button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    // The user's utility bills - starts with some sample data
    var bills by remember {
        mutableStateOf(
            listOf(
                UtilityBill(1, "Electricity", "Electric Ireland", 85.50, "2026-03-01"),
                UtilityBill(2, "Gas", "Bord Gais", 62.00, "2026-03-05"),
                UtilityBill(3, "Broadband", "Eir", 45.99, "2026-03-10")
            )
        )
    }
    var nextId by remember { mutableIntStateOf(4) }

    // Controls for the "add a bill" dialog
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Electricity") }
    var providerText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

    // The categories the user can pick from when adding a new bill
    val billCategories = listOf("Electricity", "Gas", "Broadband", "Mobile", "Car Insurance")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = White) },
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
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Green600)
            )
        },
        floatingActionButton = {
            // Floating button to open the "add a new bill" dialog
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Red500,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bill")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Dashboard heading
            Text(
                text = "Dashboard",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bar chart showing how much the user spends in each category
            if (bills.isNotEmpty()) {
                BillsBarChart(bills = bills)
            } else {
                // Show a helpful message when there are no bills yet
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Text(
                        text = "No bills added yet. Tap + to add your first utility bill.",
                        modifier = Modifier.padding(24.dp),
                        color = DarkGray,
                        fontSize = 14.sp
                    )
                }
            }

            // Extra space at the bottom so content doesn't hide behind the floating button
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Dialog that pops up when the user taps the + button to add a new bill
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Utility Bill") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Dropdown to pick the bill category
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            billCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Text fields for the provider name and bill amount
                    OutlinedTextField(
                        value = providerText,
                        onValueChange = { providerText = it },
                        label = { Text("Provider") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Amount (€)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                // Only add the bill if the provider is filled in and the amount is a valid number
                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull()
                        if (providerText.isNotBlank() && amount != null) {
                            bills = bills + UtilityBill(
                                id = nextId,
                                category = selectedCategory,
                                provider = providerText,
                                amount = amount,
                                date = "2026-03-25"
                            )
                            nextId++
                            providerText = ""
                            amountText = ""
                            selectedCategory = "Electricity"
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Green600)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// A horizontal bar chart that shows spending broken down by category.
// Each category gets a coloured bar sized relative to the highest spending category.
@Composable
fun BillsBarChart(bills: List<UtilityBill>) {
    // Add up the total amount for each category and sort highest first
    val categoryTotals = bills
        .groupBy { it.category }
        .mapValues { (_, items) -> items.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val maxAmount = categoryTotals.maxOfOrNull { it.second } ?: 0.0

    // Each category gets a different colour bar
    val barColors = listOf(
        Color(0xFF16A34A),  // Green
        Color(0xFFEF4444),  // Red
        Color(0xFF3B82F6),  // Blue
        Color(0xFFF59E0B),  // Amber
        Color(0xFF8B5CF6)   // Purple
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Spending by Category",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            categoryTotals.forEachIndexed { index, (category, amount) ->
                // Work out how wide this bar should be compared to the biggest one
                val fraction = if (maxAmount > 0) (amount / maxAmount).toFloat() else 0f
                val barColor = barColors[index % barColors.size]

                // Show the category name on the left and the amount on the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = category, fontSize = 13.sp, color = DarkGray)
                    Text(
                        text = "€${"%.2f".format(amount)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Draw the bar using Canvas - a grey background track with a coloured fill
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                ) {
                    // Grey background track
                    drawRoundRect(
                        color = Color(0xFFF5F5F5),
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Coloured fill showing the spending amount
                    if (fraction > 0f) {
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset.Zero,
                            size = Size(size.width * fraction, size.height),
                            cornerRadius = CornerRadius(8f, 8f)
                        )
                    }
                }

                // Add spacing between bars, but not after the last one
                if (index < categoryTotals.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
