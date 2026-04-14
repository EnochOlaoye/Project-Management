package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(onBackClick: () -> Unit) {
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Us", color = White) },
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
                text = "Get in Touch",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We're here to help. Reach out to us through any of the following:",
                fontSize = 14.sp,
                color = White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            ContactCard(
                icon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Green600, modifier = Modifier.size(28.dp)) },
                title = "Phone",
                detail = "+353 123 123 123",
                subtitle = "Mon - Fri, 9am - 5pm"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactCard(
                icon = { Icon(Icons.Default.Email, contentDescription = null, tint = Green600, modifier = Modifier.size(28.dp)) },
                title = "Email",
                detail = "support@solution4u.ie",
                subtitle = "We aim to respond within 24 hours"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactCard(
                icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Green600, modifier = Modifier.size(28.dp)) },
                title = "Address",
                detail = "123 Example Street",
                subtitle = "Galway, Ireland"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Business Hours",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    listOf(
                        "Monday - Friday" to "9:00am - 5:00pm",
                        "Saturday" to "10:00am - 2:00pm",
                        "Sunday" to "Closed"
                    ).forEach { (day, hours) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = day, fontSize = 14.sp, color = DarkGray)
                            Text(text = hours, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactCard(
    icon: @Composable () -> Unit,
    title: String,
    detail: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 12.sp, color = DarkGray)
                Text(text = detail, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Black)
                Text(text = subtitle, fontSize = 12.sp, color = DarkGray)
            }
        }
    }
}