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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.model.UtilityBill
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.example.solutions4u.utils.NotificationHelper
import androidx.compose.ui.platform.LocalContext
import android.print.PrintManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.solutions4u.utils.PdfReportGenerator
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onReportsClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

   // The user's utility bills - starts with some sample data
    var bills by remember {
     mutableStateOf(
        listOf(
            UtilityBill(1, "Electricity", "Electric Ireland", 85.50, "2026-04-21"),
            UtilityBill(2, "Gas", "Bord Gais", 62.00, "2026-04-23"),
            UtilityBill(3, "Broadband", "Eir", 45.99, "2026-04-28"),
            UtilityBill(4, "Mobile", "Three", 30.00, "2026-05-10"),
            UtilityBill(5, "Car Insurance", "AXA", 120.00, "2026-05-15")
        )
    )
}
    var nextId by remember { mutableIntStateOf(6) }

    // Controls for the "add a bill" dialog
    var showAddDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Electricity") }
    var providerText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var dueDateText by remember { mutableStateOf("") }
    var showPrintDialog by remember { mutableStateOf(false) }
    var selectedPrintPeriod by remember { mutableStateOf("Monthly") }

    var selectedPeriod by remember { mutableStateOf("Monthly") }
    val periods = listOf("Daily", "Weekly", "Monthly", "Yearly")

    // The categories the user can pick from when adding a new bill
    val billCategories = listOf("Electricity", "Gas", "Broadband", "Mobile", "Car Insurance")

    val filteredBills = when (selectedPeriod) {
        "Daily" -> bills.filter { it.date == "2026-04-02" }
        "Weekly" -> bills.filter { it.date >= "2026-03-26" }
        "Monthly" -> bills.filter { it.date.startsWith("2026-04") || it.date.startsWith("2026-03") }
        "Yearly" -> bills.filter { it.date.startsWith("2026") }
        else -> bills
    }

    // Today's date for calculating days until due
    val today = LocalDate.now()

    // Bills due within the next 30 days
    val upcomingBills = bills.filter { bill ->
        try {
            val dueDate = LocalDate.parse(bill.date, DateTimeFormatter.ISO_LOCAL_DATE)
            val daysUntil = ChronoUnit.DAYS.between(today, dueDate)
            daysUntil in 0..30
        } catch (e: Exception) {
            false
        }
    }.sortedBy { it.date }

    val context = LocalContext.current

    // Check for bills due tomorrow and send notifications
    LaunchedEffect(bills) {
        val tomorrow = today.plusDays(1)
        bills.forEach { bill ->
            try {
                val dueDate = LocalDate.parse(bill.date, DateTimeFormatter.ISO_LOCAL_DATE)
                if (dueDate == tomorrow) {
                    NotificationHelper.sendBillDueTomorrowNotification(
                        context = context,
                        category = bill.category,
                        provider = bill.provider,
                        amount = bill.amount
                    )
                }
            } catch (e: Exception) {
                // Invalid date format, skip
            }
        }
    }

    // Read due dates setting from DataStore
    val dueDatesEnabled by ThemeManager.getDueDatesEnabled(context)
        .collectAsState(initial = false)

    // Check for bills due tomorrow and send notifications only if enabled
    LaunchedEffect(bills, dueDatesEnabled) {
        if (!dueDatesEnabled) return@LaunchedEffect
        val tomorrow = today.plusDays(1)
        bills.forEach { bill ->
            try {
                val dueDate = LocalDate.parse(bill.date, DateTimeFormatter.ISO_LOCAL_DATE)
                if (dueDate == tomorrow) {
                    NotificationHelper.sendBillDueTomorrowNotification(
                        context = context,
                        category = bill.category,
                        provider = bill.provider,
                        amount = bill.amount
                    )
                }
            } catch (e: Exception) {
                // Invalid date format, skip
            }
        }
    }

    // Print dialog
    if (showPrintDialog) {
        AlertDialog(
            onDismissRequest = { showPrintDialog = false },
            title = { Text("Print Report", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select period to print:", fontSize = 14.sp, color = DarkGray)
                    periods.forEach { period ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPrintPeriod = period }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedPrintPeriod == period,
                                onClick = { selectedPrintPeriod = period },
                                colors = RadioButtonDefaults.colors(selectedColor = Green600)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = period, fontSize = 15.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPrintDialog = false
                        // Get bills for selected period
                        val printBills = when (selectedPrintPeriod) {
                            "Daily" -> bills.filter { it.date == "2026-04-21" }
                            "Weekly" -> bills.filter { it.date >= "2026-04-15" }
                            "Monthly" -> bills.filter { it.date.startsWith("2026-04") || it.date.startsWith("2026-05") }
                            "Yearly" -> bills.filter { it.date.startsWith("2026") }
                            else -> bills
                        }
                        // Generate PDF
                        val file = PdfReportGenerator.generateReport(context, selectedPrintPeriod, printBills)
                        if (file != null) {
                            // Open print dialog
                            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                            val jobName = "Solutions4U_$selectedPrintPeriod"
                            val fileUri = androidx.core.content.FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                setDataAndType(fileUri, "application/pdf")
                                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Green600)
                ) {
                    Text("Generate & Open", color = White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showPrintDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background.darken())
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

            // Upcoming bills section - shows bills due in the next 30 days
            if (upcomingBills.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upcomingBillsSection"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Upcoming Charges",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        upcomingBills.forEach { bill ->
                            // Calculate days until the bill is due
                            val dueDate = LocalDate.parse(bill.date, DateTimeFormatter.ISO_LOCAL_DATE)
                            val daysUntil = ChronoUnit.DAYS.between(today, dueDate)
                            val dueText = when {
                                daysUntil == 0L -> "Due today!"
                                daysUntil == 1L -> "Due tomorrow"
                                daysUntil <= 7L -> "Due in $daysUntil days"
                                else -> "Due on ${bill.date}"
                            }
                            // Highlight urgent bills in red
                            val cardColor = when {
                                daysUntil <= 3 -> Color(0xFFFFEBEE)
                                daysUntil <= 7 -> Color(0xFFFFF3E0)
                                else -> Color(0xFFF1F8E9)
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .testTag("upcomingBill_${bill.id}"),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = bill.category,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Black
                                        )
                                        Text(
                                            text = bill.provider,
                                            fontSize = 12.sp,
                                            color = DarkGray
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "€${"%.2f".format(bill.amount)}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Black
                                        )
                                        Text(
                                            text = dueText,
                                            fontSize = 11.sp,
                                            color = if (daysUntil <= 3) Color.Red else DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Swipeable tabs
            // Time period tabs
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("periodTabs"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    periods.forEach { period ->
                        Button(
                            onClick = { selectedPeriod = period },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedPeriod == period) Green600 else Color.Transparent,
                                contentColor = if (selectedPeriod == period) White else DarkGray
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("tab_$period")
                        ) {
                            Text(text = period, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total for selected period
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "$selectedPeriod Total",
                        fontSize = 14.sp,
                        color = DarkGray
                    )
                    Text(
                        text = "€${"%.2f".format(filteredBills.sumOf { it.amount })}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green600
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show chart or empty message
            if (filteredBills.isNotEmpty()) {
                BillsBarChart(bills = filteredBills)
            } else {
                // Show a helpful message when there are no bills yet
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Text(
                        text = "No bills found for this period. Tap + to add a bill.",
                        modifier = Modifier.padding(24.dp),
                        color = DarkGray,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showPrintDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background.darken()),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Print / Save PDF", color = White, fontSize = 13.sp)
                }
                Button(
                    onClick = { onReportsClick() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background.darken()),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Previous Reports", color = White, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Reset dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Bills") },
            text = { Text("This will clear all your bills and restore the sample data. Are you sure?") },
            confirmButton = {
                Button(
                    onClick = {
                        bills = listOf(
                            UtilityBill(1, "Electricity", "Electric Ireland", 85.50, "2026-04-02"),
                            UtilityBill(2, "Gas", "Bord Gais", 62.00, "2026-03-28"),
                            UtilityBill(3, "Broadband", "Eir", 45.99, "2026-03-10"),
                            UtilityBill(4, "Mobile", "Three", 30.00, "2026-02-15"),
                            UtilityBill(5, "Car Insurance", "AXA", 120.00, "2026-01-01")
                        )
                        nextId = 6
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) {
                    Text("Reset", color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add bill dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Utility Bill") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                    // Due date field for tracking when the bill is charged
                    OutlinedTextField(
                        value = dueDateText,
                        onValueChange = { dueDateText = it },
                        label = { Text("Due Date (YYYY-MM-DD)") },
                        singleLine = true,
                        placeholder = { Text("e.g. 2026-05-01") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull()
                        val date = if (dueDateText.isNotBlank()) dueDateText else "2026-04-02"
                        if (providerText.isNotBlank() && amount != null) {
                            bills = bills + UtilityBill(
                                id = nextId,
                                category = selectedCategory,
                                provider = providerText,
                                amount = amount,
                                date = date
                            )
                            nextId++
                            providerText = ""
                            amountText = ""
                            dueDateText = ""
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

@Composable
fun BillsBarChart(bills: List<UtilityBill>) {
    val categoryTotals = bills
        .groupBy { it.category }
        .mapValues { (_, items) -> items.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val maxAmount = categoryTotals.maxOfOrNull { it.second } ?: 0.0

    val barColors = listOf(
        Color(0xFF16A34A),
        Color(0xFFEF4444),
        Color(0xFF3B82F6),
        Color(0xFFF59E0B),
        Color(0xFF8B5CF6)
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
                val fraction = if (maxAmount > 0) (amount / maxAmount).toFloat() else 0f
                val barColor = barColors[index % barColors.size]
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
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)) {
                    drawRoundRect(
                        color = Color(0xFFF5F5F5),
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    if (fraction > 0f) {
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset.Zero,
                            size = Size(size.width * fraction, size.height),
                            cornerRadius = CornerRadius(8f, 8f)
                        )
                    }
                }
                if (index < categoryTotals.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}