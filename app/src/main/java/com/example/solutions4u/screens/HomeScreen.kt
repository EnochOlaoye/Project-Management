package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.model.Category
import com.example.solutions4u.model.NewsItem
import com.example.solutions4u.model.categories
import com.example.solutions4u.model.newsItems
import com.example.solutions4u.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    onSignInClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = White,
                        modifier = Modifier.padding(8.dp)
                    )
                },
                actions = {
                    // Navigation chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val navItems = listOf("Electricity", "Gas", "Insurance", "Broadband", "Mobile", "News")
                        items(navItems) { item ->
                            AssistChip(
                                onClick = { onCategoryClick(item.lowercase()) },
                                label = { Text(item, fontSize = 11.sp, color = White) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Green600
                                ),
                                border = null
                            )
                        }
                        item {
                            OutlinedButton(
                                onClick = onSignInClick,
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Text("Sign In", fontSize = 11.sp, color = White)
                            }
                        }
                        item {
                            Button(
                                onClick = onRegisterClick,
                                modifier = Modifier.height(32.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Red500),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Text("Register", fontSize = 11.sp)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Green500
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            // Hero Section
            HeroSection()

            // Category Cards Section
            CategoryCardsSection(onCategoryClick = onCategoryClick)

            // News Section
            NewsSection()

            // Footer
            FooterSection()
        }
    }
}

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green500)
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solutions 4 U",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color.Transparent,
            border = ButtonDefaults.outlinedButtonBorder(true),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Cost analysis and saving",
                fontSize = 16.sp,
                color = White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Two image placeholder cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dollar sign card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = Red500
                    )
                }
            }
            // Piggy bank card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8BBD0))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\uD83D\uDC37",
                        fontSize = 64.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCardsSection(onCategoryClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green500)
            .padding(16.dp)
    ) {
        Text(
            text = "Start saving now",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Text(
            text = "We compare and switch so you don't have the hassle.",
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Category grid - 2 columns x 3 rows
        val rows = categories.chunked(3)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining space if row is not complete
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = LightGray
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        tint = Green600,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Black
            )

            Text(
                text = category.description,
                fontSize = 10.sp,
                color = DarkGray,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun NewsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green500)
            .padding(16.dp)
    ) {
        Text(
            text = "News",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Text(
            text = "Current climate",
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        newsItems.forEach { newsItem ->
            NewsCard(newsItem = newsItem)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun NewsCard(newsItem: NewsItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Red500),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail placeholder
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = White
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = when (newsItem.category) {
                            "Electricity" -> "\u26A1"
                            "Insurance" -> "\uD83D\uDE97"
                            else -> "\uD83D\uDCF0"
                        },
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = newsItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = White
                )
                Text(
                    text = newsItem.description,
                    fontSize = 12.sp,
                    color = White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Take me to article",
                        fontSize = 11.sp,
                        color = Black
                    )
                }
            }
        }
    }
}

@Composable
fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkGray)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Use cases column
            Column {
                Text("Use cases", fontWeight = FontWeight.Bold, color = White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                listOf("UI design", "UX design", "Wireframing", "Diagramming", "Brainstorming").forEach {
                    Text(it, color = White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
            // Explore column
            Column {
                Text("Explore", fontWeight = FontWeight.Bold, color = White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                listOf("Design", "Prototyping", "Development features", "Design systems").forEach {
                    Text(it, color = White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
            // Resources column
            Column {
                Text("Resources", fontWeight = FontWeight.Bold, color = White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                listOf("Blog", "Best practices", "Colors", "Color wheel", "Support").forEach {
                    Text(it, color = White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }
    }
}
