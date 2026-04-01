package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Person
import kotlinx.coroutines.launch
import com.example.solutions4u.network.AuthRepository
import com.example.solutions4u.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userId: String,
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit,
    onLogout: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val repository = remember { AuthRepository() }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "Delete Account",
                    fontWeight = FontWeight.Bold,
                    color = Red500
                )
            },
            text = {
                Text("Are you sure you want to delete your account? This cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            val result = repository.deleteAccount(userId)
                            if (result) {
                                showConfirmDialog = false
                                onAccountDeleted()
                            } else {
                                errorMessage = "Failed to delete account. Please try again."
                                showConfirmDialog = false
                            }
                            isLoading = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = White) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Green500)
                .padding(24.dp)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
            modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            IconButton(
            onClick = { /* no function yet */ },
            modifier = Modifier
            .size(100.dp) // makes it large
            .background(Green600, shape = RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.Person, // choose any icon you like
                contentDescription = "Profile Icon",
                tint = White,
             modifier = Modifier.size(48.dp)
        )   
    }
}
        Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Property Name Temp",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = White
         )

            Spacer(modifier = Modifier.height(16.dp))

             Button(
        onClick = { /* no function yet */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Green600),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "Change Details",
            color = White,
            fontWeight = FontWeight.Bold
        )
    }

     Spacer(modifier = Modifier.height(12.dp))

 Button(
        onClick = { /* no function yet */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Green600),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "Change Login Info",
            color = White,
            fontWeight = FontWeight.Bold
        )
    }

Spacer(modifier = Modifier.height(12.dp))

 Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
Button(
            onClick = { /* no function yet */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Green600),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Add",
                color = White,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { /* no function yet */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Red500),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Delete",
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

            Column(
                modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green600),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Logout",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            OutlinedButton(
                onClick = { showConfirmDialog = true },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Red500, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Red500)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Red500,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Delete Account",
                        color = Red500,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
    }
    }
}