package com.example.solutions4u.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.solutions4u.utils.PdfReportGenerator
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(onBackClick: () -> Unit) {
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()
    val context = LocalContext.current

    // Load existing reports
    var reports by remember { mutableStateOf(PdfReportGenerator.getExistingReports()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Previous Reports", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgColor)
                .padding(16.dp)
        ) {
            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No reports generated yet.\nGo to your profile and tap Print to create one.",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "${reports.size} report${if (reports.size != 1) "s" else ""} found",
                    color = White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(reports) { file ->
                        ReportCard(
                            file = file,
                            onClick = {
                                // Open PDF with system viewer
                                try {
                                    val uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.provider",
                                        file
                                    )
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, "application/pdf")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            },
                            onDelete = {
                                file.delete()
                                reports = PdfReportGenerator.getExistingReports()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCard(
    file: File,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateModified = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        .format(Date(file.lastModified()))

    // Parse period from filename e.g. Solutions4U_Monthly_1234567890.pdf
    val period = file.name.removePrefix("Solutions4U_").split("_").firstOrNull() ?: "Unknown"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$period Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Black
                )
                Text(
                    text = dateModified,
                    fontSize = 12.sp,
                    color = DarkGray
                )
                Text(
                    text = "Tap to open",
                    fontSize = 11.sp,
                    color = Green600
                )
            }
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(contentColor = Red500)
            ) {
                Text("Delete", fontSize = 12.sp)
            }
        }
    }
}