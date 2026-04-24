package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.model.CompanyReview
import com.example.solutions4u.model.companyReviews
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(onBackClick: () -> Unit) {
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()

    var query by remember { mutableStateOf("") }

    // Filter reviews based on search query
    val filteredReviews = remember(query) {
        if (query.isBlank()) companyReviews
        else {
            val normalised = query.lowercase()
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u")
            companyReviews.filter { review ->
                val companyNormalised = review.company.lowercase()
                    .replace("á", "a").replace("é", "e")
                    .replace("í", "i").replace("ó", "o")
                    .replace("ú", "u")
                val categoryNormalised = review.category.lowercase()
                companyNormalised.contains(normalised) || categoryNormalised.contains(normalised)
            }
        }
    }

    // Group reviews by company
    val groupedReviews = filteredReviews.groupBy { it.company }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Company Reviews", color = White) },
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
                placeholder = {
                    Text(
                        "Search company or category...",
                        color = White.copy(alpha = 0.5f)
                    )
                },
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

            if (groupedReviews.isEmpty()) {
                Text(
                    text = "No reviews found for \"$query\"",
                    color = White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    groupedReviews.forEach { (company, reviews) ->
                        item {
                            CompanyReviewSection(
                                company = company,
                                category = reviews.first().category,
                                reviews = reviews
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompanyReviewSection(
    company: String,
    category: String,
    reviews: List<CompanyReview>
) {
    val avgRating = reviews.map { it.rating }.average()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Company header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = company,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Black
                    )
                    Text(
                        text = category,
                        fontSize = 13.sp,
                        color = DarkGray
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Green600,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${"%.1f".format(avgRating)}/5",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Green600
                        )
                    }
                    Text(
                        text = "${reviews.size} review${if (reviews.size != 1) "s" else ""}",
                        fontSize = 11.sp,
                        color = DarkGray
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Individual reviews
            reviews.forEach { review ->
                ReviewItem(review = review)
                if (review != reviews.last()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: CompanyReview) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.reviewer,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Black
            )
            Text(
                text = review.date,
                fontSize = 11.sp,
                color = DarkGray
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Star rating
        Row {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < review.rating) Green600 else LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = review.comment,
            fontSize = 13.sp,
            color = DarkGray,
            lineHeight = 20.sp
        )
    }
}