package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.solutions4u.network.AuthRepository
import com.example.solutions4u.network.Property
import com.example.solutions4u.network.PropertyRepository
import com.example.solutions4u.network.PropertyResult
import com.example.solutions4u.ui.theme.*
import com.example.solutions4u.ui.theme.darken
import com.example.solutions4u.network.AuthResult

// Add Property Dialog 
@Composable
private fun AddPropertyDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, addressLine1: String, addressLine2: String, eircode: String) -> Unit
) {
    var name         by remember { mutableStateOf("") }
    var addressLine1 by remember { mutableStateOf("") }
    var addressLine2 by remember { mutableStateOf("") }
    var eircode      by remember { mutableStateOf("") }
    var saveAttempted by remember { mutableStateOf(false) }

    // Required fields: name, addressLine1, eircode. addressLine2 is optional.
    val nameError    = saveAttempted && name.isBlank()
    val addr1Error   = saveAttempted && addressLine1.isBlank()
    val eircodeError = saveAttempted && eircode.isBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Property", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Property Name") },
                    isError       = nameError,
                    supportingText = if (nameError) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = addressLine1,
                    onValueChange = { addressLine1 = it },
                    label         = { Text("Address Line 1") },
                    isError       = addr1Error,
                    supportingText = if (addr1Error) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = addressLine2,
                    onValueChange = { addressLine2 = it },
                    label         = { Text("Address Line 2 (optional)") },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = eircode,
                    onValueChange = { eircode = it },
                    label         = { Text("Eircode") },
                    isError       = eircodeError,
                    supportingText = if (eircodeError) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    saveAttempted = true
                    if (name.isNotBlank() && addressLine1.isNotBlank() && eircode.isNotBlank()) {
                        onSave(name.trim(), addressLine1.trim(), addressLine2.trim(), eircode.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save", color = White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Edit property dialog
@Composable
private fun EditPropertyDialog(
    property: Property,
    onDismiss: () -> Unit,
    onSave: (name: String, addressLine1: String, addressLine2: String, eircode: String) -> Unit
) {
    var name         by remember { mutableStateOf(property.name) }
    var addressLine1 by remember { mutableStateOf(property.addressLine1) }
    var addressLine2 by remember { mutableStateOf(property.addressLine2) }
    var eircode      by remember { mutableStateOf(property.eircode) }
    var saveAttempted by remember { mutableStateOf(false) }

    val nameError    = saveAttempted && name.isBlank()
    val addr1Error   = saveAttempted && addressLine1.isBlank()
    val eircodeError = saveAttempted && eircode.isBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Property", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Property Name") },
                    isError       = nameError,
                    supportingText = if (nameError) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = addressLine1,
                    onValueChange = { addressLine1 = it },
                    label         = { Text("Address Line 1") },
                    isError       = addr1Error,
                    supportingText = if (addr1Error) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = addressLine2,
                    onValueChange = { addressLine2 = it },
                    label         = { Text("Address Line 2 (optional)") },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = eircode,
                    onValueChange = { eircode = it },
                    label         = { Text("Eircode") },
                    isError       = eircodeError,
                    supportingText = if (eircodeError) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    saveAttempted = true
                    if (name.isNotBlank() && addressLine1.isNotBlank() && eircode.isNotBlank()) {
                        onSave(name.trim(), addressLine1.trim(), addressLine2.trim(), eircode.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save", color = White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Property List Dialog (switch or delete) 
@Composable
private fun PropertyListDialog(
    title: String,
    properties: List<Property>,
    onDismiss: () -> Unit,
    onSelect: (Property) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            if (properties.isEmpty()) {
                Text("No properties saved yet.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(properties) { property ->
                        Card(
                            modifier  = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(property) },
                            shape     = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(property.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(property.addressLine1, fontSize = 13.sp, color = Color.Gray)
                                if (property.addressLine2.isNotBlank()) {
                                    Text(property.addressLine2, fontSize = 13.sp, color = Color.Gray)
                                }
                                Text(property.eircode, fontSize = 13.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

// Delete Confirmation Dialog 
@Composable
private fun DeleteConfirmDialog(
    property: Property,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Property", fontWeight = FontWeight.Bold, color = Red500) },
        text  = { Text("Are you sure you want to delete \"${property.name}\"? This cannot be undone.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(containerColor = Red500)
            ) {
                Text("Yes", color = White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("No") }
        }
    )
}

@Composable
private fun EditLoginInfoDialog(
    currentName: String,
    currentEmail: String,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, password: String?) -> Unit
) {
    var name          by remember { mutableStateOf(currentName) }
    var email         by remember { mutableStateOf(currentEmail) }
    var password      by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var saveAttempted by remember { mutableStateOf(false) }

    val nameError     = saveAttempted && name.isBlank()
    val emailError    = saveAttempted && email.isBlank()
    val passwordMismatch = saveAttempted && password != confirmPassword
    val passwordTooShort = saveAttempted && password.isNotBlank() && password.length < 6

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Login Info", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Full Name") },
                    isError       = nameError,
                    supportingText = if (nameError) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = email,
                    onValueChange = { email = it },
                    label         = { Text("Email") },
                    isError       = emailError,
                    supportingText = if (emailError) {{ Text("Required", color = Red500) }} else null,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = password,
                    onValueChange = { password = it },
                    label         = { Text("New Password (leave blank to keep current)") },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    isError       = passwordMismatch || passwordTooShort,
                    supportingText = when {
                        passwordTooShort -> {{ Text("Password must be at least 6 characters", color = Red500) }}
                        passwordMismatch -> {{ Text("Passwords do not match", color = Red500) }}
                        else -> null
                    },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value         = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label         = { Text("Confirm New Password") },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    isError       = passwordMismatch,
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedTextColor   = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    saveAttempted = true
                    val passwordValid = password.isBlank() || (password == confirmPassword && password.length >= 6)
                    if (name.isNotBlank() && email.isNotBlank() && passwordValid) {
                        onSave(name.trim(), email.trim(), password.ifBlank { null })
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save", color = White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userId: String,
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit,
    onLogout: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val repository = remember { AuthRepository() }
    val context = LocalContext.current
    val bgColor = MaterialTheme.colorScheme.background
    val topBarColor = bgColor.darken()

    var showAddProperty    by remember { mutableStateOf(false) }
    var showEditProperty by remember { mutableStateOf(false) }
    var showSwitchProperty by remember { mutableStateOf(false) }
    var showDeleteList     by remember { mutableStateOf(false) }
    var showEditLoginInfo by remember { mutableStateOf(false) }
    var propertyToDelete   by remember { mutableStateOf<Property?>(null) }
    var properties     by remember { mutableStateOf<List<Property>>(emptyList()) }
    var activeProperty by remember { mutableStateOf<Property?>(null) }
    val propertyRepository = remember { PropertyRepository() }
    val userIdInt = userId.toIntOrNull() ?: 0

    // Collect saved colour from DataStore
    val savedColorHex by ThemeManager.getBackgroundColor(context)
        .collectAsState(initial = ThemeManager.DEFAULT_COLOR)

    val backgroundColor = remember(savedColorHex) {
        parseHexColor(savedColorHex) ?: Color(0xFF2E7D32)
    }

    // Load properties on first launch
    LaunchedEffect(userIdInt) {
        if (userIdInt == 0) return@LaunchedEffect
        isLoading = true
        when (val result = propertyRepository.getProperties(userIdInt)) {
            is PropertyResult.Success -> {
                properties     = result.properties
                activeProperty = properties.firstOrNull()
            }
            is PropertyResult.Error -> errorMessage = result.message
        }
        isLoading = false
    }

    // Theme Dialog
    if (showThemeDialog) {
        ThemeColorDialog(
            currentHex = savedColorHex,
            onDismiss = { showThemeDialog = false },
            onSave = { hex ->
                scope.launch {
                    ThemeManager.saveBackgroundColor(context, hex)
                }
                showThemeDialog = false
            }
        )
    }

    // Add property
    if (showAddProperty) {
        AddPropertyDialog(
            onDismiss = { showAddProperty = false },
            onSave    = { name, addressLine1, addressLine2, eircode ->
                showAddProperty = false
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    when (val result = propertyRepository.addProperty(userIdInt, name, addressLine1, addressLine2, eircode)) {
                        is PropertyResult.Success -> {
                            // Use the property returned by the server (has the real DB id)
                            val saved = result.property
                            if (saved != null) {
                                properties     = properties + saved
                                activeProperty = saved
                            } else {
                                // Fallback: reload all properties from server
                                when (val reload = propertyRepository.getProperties(userIdInt)) {
                                    is PropertyResult.Success -> {
                                        properties     = reload.properties
                                        activeProperty = properties.lastOrNull()
                                    }
                                    is PropertyResult.Error -> errorMessage = reload.message
                                }
                            }
                        }
                        is PropertyResult.Error -> errorMessage = result.message
                    }
                    isLoading = false
                }
            }
        )
    }

    // Edit active property
    if (showEditProperty && activeProperty != null) {
        EditPropertyDialog(
            property  = activeProperty!!,
            onDismiss = { showEditProperty = false },
            onSave    = { name, addressLine1, addressLine2, eircode ->
                showEditProperty = false
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    when (val result = propertyRepository.updateProperty(
                        activeProperty!!.id, name, addressLine1, addressLine2, eircode
                    )) {
                        is PropertyResult.Success -> {
                            val updated = activeProperty!!.copy(
                                name = name,
                                addressLine1 = addressLine1,
                                addressLine2 = addressLine2,
                                eircode = eircode
                            )
                            properties = properties.map {
                                if (it.id == updated.id) updated else it
                            }
                            activeProperty = updated
                        }
                        is PropertyResult.Error -> errorMessage = result.message
                    }
                    isLoading = false
                }
            }
        )
    }

    // Edit login info
    if (showEditLoginInfo) {
        EditLoginInfoDialog(
            currentName  = userName,
            currentEmail = userEmail,
            onDismiss    = { showEditLoginInfo = false },
            onSave       = { name: String, email: String, password: String? ->
                showEditLoginInfo = false
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    when (val result = repository.updateUser(userId, name, email, password)) {
                        is AuthResult.Success -> { /* updated successfully */ }
                        is AuthResult.Error   -> errorMessage = result.message
                    }
                    isLoading = false
                }
            }
        )
    }

    // Switch property (icon button)
    if (showSwitchProperty) {
        PropertyListDialog(
            title      = "Select Property",
            properties = properties,
            onDismiss  = { showSwitchProperty = false },
            onSelect   = { property ->
                activeProperty     = property
                showSwitchProperty = false
            }
        )
    }

    // Delete — step 1: pick which property
    if (showDeleteList && propertyToDelete == null) {
        PropertyListDialog(
            title      = "Delete a Property",
            properties = properties,
            onDismiss  = { showDeleteList = false },
            onSelect   = { property -> propertyToDelete = property }
        )
    }

    // Delete — step 2: confirm
    propertyToDelete?.let { prop ->
        DeleteConfirmDialog(
            property  = prop,
            onDismiss = {
                propertyToDelete = null
                showDeleteList   = false
            },
            onConfirm = {
                val targetId = prop.id
                propertyToDelete = null
                showDeleteList   = false
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    when (val result = propertyRepository.deleteProperty(targetId)) {
                        is PropertyResult.Success -> {
                            properties = properties.filter { it.id != targetId }
                            if (activeProperty?.id == targetId) {
                                activeProperty = properties.firstOrNull()
                            }
                        }
                        is PropertyResult.Error -> errorMessage = result.message
                    }
                    isLoading = false
                }
            }
        )
    }

    // Delete account dialog
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

    // Main layout
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bgColor)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile icon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    IconButton(
                        onClick = { showSwitchProperty = true },
                        modifier = Modifier
                            .size(100.dp) // makes it large
                            .background(topBarColor, shape = RoundedCornerShape(50))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon",
                            tint = White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Buttons in middle of screen
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = activeProperty?.name ?: "No Property Selected",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showEditProperty = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = topBarColor),
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
                        onClick = { showEditLoginInfo = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = topBarColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Change Login Info",
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Theme button
                    Button(
                        onClick = { showThemeDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = topBarColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Theme",
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
                            onClick = { showAddProperty = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = topBarColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Add",
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { showDeleteList = true },
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

                // Bottom Buttons
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = { onLogout() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = topBarColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Logout",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Button(
                        onClick = { showConfirmDialog = true },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Red500),
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
                                color = White,
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