package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About", color = White) },
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
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Our Story",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "We decided to build a utility price comparison app because, honestly, everyone's feeling the sting of higher bills these days. Electricity, internet, gas, mobile plans, insurance—you name it, the prices keep climbing. People want to save money, but actually tracking down the best deals is a headache. You end up calling around or digging through random websites, and the whole thing just takes forever.",
                        fontSize = 15.sp,
                        color = DarkGray,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "With this project, we want to cut through that hassle. The app pulls live prices from different providers and puts everything in one spot. No more bouncing between tabs or making endless phone calls. Just clear, side-by-side quotes, so you can spot the best option right away and maybe even save a chunk of change.",
                        fontSize = 15.sp,
                        color = DarkGray,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "We took some inspiration from those big price comparison websites, but we're going mobile-first this time. It just makes sense—everyone manages life on their phone now. Plus, the app isn't just handy for regular folks. There are business angles too, since it connects companies to customers and even other businesses. That gives it some real weight, both technically and commercially.",
                        fontSize = 15.sp,
                        color = DarkGray,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "So, why this idea? Because it actually helps people, fits the times, and solves a problem almost everyone deals with.",
                        fontSize = 15.sp,
                        color = DarkGray,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}