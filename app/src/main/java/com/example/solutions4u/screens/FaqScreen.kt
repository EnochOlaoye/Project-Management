package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.solutions4u.ui.theme.White
import com.example.solutions4u.ui.theme.darken
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(onBackClick: () -> Unit) {
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQ", color = White) },
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
                .padding(24.dp)
        ) {
            Text(
                text = "Frequently Asked Questions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}