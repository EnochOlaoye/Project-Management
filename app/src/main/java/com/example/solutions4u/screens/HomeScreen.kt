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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.model.Category
import com.example.solutions4u.model.NewsItem
import com.example.solutions4u.model.categories
import com.example.solutions4u.model.newsItems
import com.example.solutions4u.network.UserData
import com.example.solutions4u.ui.theme.*

// The main landing page of the app.
// Shows a top bar with navigation chips, a hero banner, category cards, news, and a footer.
// If the user is logged in, a profile button replaces the Sign In / Register buttons.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    onSignInClick: () -> Unit,
    onRegisterClick: () -> Unit,
    loggedInUser: UserData? = null,
    onProfileClick: () -> Unit = {}
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
                    // Scrollable row of quick navigation chips and auth buttons
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
                                colors = AssistChipDefaults.assistChipColors(containerColor = Green600),
                                border = null
                            )
                        }

                        // Show the user's name button if logged in, otherwise show Sign In and Register
                        if (loggedInUser != null) {
                            item {
                                Button(
                                    onClick = onProfileClick,
                                    modifier = Modifier.height(32.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                ) {
                                    Text(loggedInUser.name, fontSize = 11.sp)
                                }
                            }
                        } else {
                            item {
                                OutlinedButton(
                                    onClick = onSignInClick,
                                    modifier = Modifier
                                        .height(32.dp)
                                        .testTag("signInButton"),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                ) {
                                    Text("Sign In", fontSize = 11.sp, color = White)
                                }
                            }
                            item {
                                Button(
                                    onClick = onRegisterClick,
                                    modifier = Modifier
                                        .height(32.dp)
                                        .testTag("registerButton"),
                                    colors = ButtonDefaults.buttonColors(containerColor = Red500),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                ) {
                                    Text("Register", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Green500)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            HeroSection()
            CategoryCardsSection(onCategoryClick = onCategoryClick)
            NewsSection()
            FooterSection()
        }
    }
}

// The big banner at the top of the home page with the app name and tagline
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

        // Two placeholder image cards side by side
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    Text(text = "\uD83D\uDC37", fontSize = 64.sp)
                }
            }
        }
    }
}

// Grid of category cards showing the six utility types the user can compare
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

        // Lay out the categories in rows of 3
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
                // Fill any empty space if the row has fewer than 3 items
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// A single category card showing the icon, name, and description
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
            // Category icon in a small box
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

// The news section on the home page showing the latest articles
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

// A single news article card with a thumbnail, title, description, and a link button
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
            // Thumbnail with a category-specific icon
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

// The dark footer at the bottom of the home page with links grouped into three columns
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
            Column {
                Text("Use cases", fontWeight = FontWeight.Bold, color = White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                listOf("UI design", "UX design", "Wireframing", "Diagramming", "Brainstorming").forEach {
                    Text(it, color = White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
            Column {
                Text("Explore", fontWeight = FontWeight.Bold, color = White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                listOf("Design", "Prototyping", "Development features", "Design systems").forEach {
                    Text(it, color = White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
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