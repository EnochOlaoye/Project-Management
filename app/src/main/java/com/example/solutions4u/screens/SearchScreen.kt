package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.model.SearchableCompany
import com.example.solutions4u.model.searchCompanies
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onCompanyClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { searchCompanies(query) }
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search", color = White) },
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
            // Search bar
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("e.g. Bord Gais, Vodafone...", color = White.copy(alpha = 0.5f)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = White)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = White,
                    unfocusedBorderColor = White.copy(alpha = 0.5f),
                    cursorColor = White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                query.isBlank() -> {
                    Text(
                        text = "Search for a provider to compare plans",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                results.isEmpty() -> {
                    Text(
                        text = "No providers found for \"$query\"",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    Text(
                        text = "${results.size} result${if (results.size != 1) "s" else ""} found",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(results) { company ->
                            SearchResultCard(
                                company = company,
                                onClick = { onCompanyClick(company.route) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(
    company: SearchableCompany,
    onClick: () -> Unit
) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = company.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Black
                )
                Text(
                    text = company.category,
                    fontSize = 13.sp,
                    color = DarkGray
                )
            }
            // Placeholder image box
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(LightGray, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = company.name.first().toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )
            }
        }
    }
}