package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit
) {
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Green600)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Green500)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(40.dp),
                        color = Green600
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = userName.take(1).uppercase(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = userName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = userEmail,
                        fontSize = 16.sp,
                        color = DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "User ID: $userId",
                        fontSize = 13.sp,
                        color = DarkGray
                    )
                }
            }
        }
    }
}