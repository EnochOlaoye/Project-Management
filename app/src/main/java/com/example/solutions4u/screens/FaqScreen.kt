package com.example.solutions4u.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.network.Faq
import com.example.solutions4u.network.FaqRepository
import com.example.solutions4u.network.FaqResult
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(
    onBackClick: () -> Unit,
    isAdmin: Boolean = false
) {
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()
    val scope = rememberCoroutineScope()
    val repository = remember { FaqRepository() }

    var faqs by remember { mutableStateOf<List<Faq>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }
    var faqToEdit by remember { mutableStateOf<Faq?>(null) }
    var faqToDelete by remember { mutableStateOf<Faq?>(null) }

    // Load FAQs on launch
    LaunchedEffect(Unit) {
        when (val result = repository.getFaqs()) {
            is FaqResult.Success -> faqs = result.faqs
            is FaqResult.Error -> errorMessage = result.message
        }
        isLoading = false
    }

    // Add/Edit dialog
    if (showAddDialog || faqToEdit != null) {
        FaqEditDialog(
            existing = faqToEdit,
            onDismiss = {
                showAddDialog = false
                faqToEdit = null
            },
            onSave = { question, answer ->
                scope.launch {
                    val result = if (faqToEdit != null) {
                        repository.updateFaq(faqToEdit!!.id, question, answer)
                    } else {
                        repository.addFaq(question, answer)
                    }
                    when (result) {
                        is FaqResult.Success -> {
                            // Reload FAQs from server
                            when (val reload = repository.getFaqs()) {
                                is FaqResult.Success -> faqs = reload.faqs
                                is FaqResult.Error -> errorMessage = reload.message
                            }
                        }
                        is FaqResult.Error -> errorMessage = result.message
                    }
                    showAddDialog = false
                    faqToEdit = null
                }
            }
        )
    }

    // Delete confirmation dialog
    faqToDelete?.let { faq ->
        AlertDialog(
            onDismissRequest = { faqToDelete = null },
            title = { Text("Delete FAQ", fontWeight = FontWeight.Bold, color = Red500) },
            text = { Text("Are you sure you want to delete this FAQ?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            when (repository.deleteFaq(faq.id)) {
                                is FaqResult.Success -> faqs = faqs.filter { it.id != faq.id }
                                is FaqResult.Error -> errorMessage = "Failed to delete FAQ"
                            }
                            faqToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) { Text("Delete", color = White) }
            },
            dismissButton = {
                OutlinedButton(onClick = { faqToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQ", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Red500,
                    contentColor = White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add FAQ")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgColor)
                .padding(16.dp)
        ) {
            when {
                isLoading -> CircularProgressIndicator(
                    color = White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                errorMessage != null -> Text(
                    text = errorMessage!!,
                    color = Red500,
                    modifier = Modifier.padding(16.dp)
                )
                faqs.isEmpty() -> Text(
                    text = "No FAQs yet.",
                    color = White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(faqs) { faq ->
                        FaqItem(
                            faq = faq,
                            isAdmin = isAdmin,
                            onEdit = { faqToEdit = faq },
                            onDelete = { faqToDelete = faq }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FaqItem(
    faq: Faq,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Black,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    if (isAdmin) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DarkGray)
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Red500)
                        }
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = DarkGray
                        )
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = faq.answer,
                    fontSize = 14.sp,
                    color = DarkGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FaqEditDialog(
    existing: Faq?,
    onDismiss: () -> Unit,
    onSave: (question: String, answer: String) -> Unit
) {
    var question by remember { mutableStateOf(existing?.question ?: "") }
    var answer by remember { mutableStateOf(existing?.answer ?: "") }
    var saveAttempted by remember { mutableStateOf(false) }

    val questionError = saveAttempted && question.isBlank()
    val answerError = saveAttempted && answer.isBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (existing != null) "Edit FAQ" else "Add a FAQ",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    isError = questionError,
                    supportingText = if (questionError) {{ Text("Required", color = Red500) }} else null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Answer") },
                    isError = answerError,
                    supportingText = if (answerError) {{ Text("Required", color = Red500) }} else null,
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    saveAttempted = true
                    if (question.isNotBlank() && answer.isNotBlank()) {
                        onSave(question.trim(), answer.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Save", color = White, fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}