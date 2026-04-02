package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class ColorSwatch(val label: String, val hex: String, val color: Color)

val presetSwatches = listOf(
    ColorSwatch("Green",  "2E7D32", Color(0xFF2E7D32)),
    ColorSwatch("Red",    "C62828", Color(0xFFC62828)),
    ColorSwatch("Blue",   "1565C0", Color(0xFF1565C0)),
    ColorSwatch("Yellow", "F9A825", Color(0xFFF9A825)),
    ColorSwatch("White",  "F5F5F5", Color(0xFFF5F5F5)),
    ColorSwatch("Purple", "6A1B9A", Color(0xFF6A1B9A)),
)

fun parseHexColor(hex: String): Color? {
    val clean = hex.removePrefix("#").trim()
    if (clean.length != 6) return null
    return try {
        val colorInt = clean.toLong(16).toInt()
        Color(0xFF000000.toInt() or colorInt)
    } catch (e: NumberFormatException) {
        null
    }
}

@Composable
fun ThemeColorDialog(
    currentHex: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var selectedHex by remember { mutableStateOf(currentHex.removePrefix("#")) }
    var hexInput by remember { mutableStateOf(currentHex.removePrefix("#")) }
    var hexError by remember { mutableStateOf(false) }

    val previewColor = parseHexColor(selectedHex) ?: Color(0xFF2E7D32)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E1E),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Change background colour",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Colour swatches + hex input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 6 colour swatches
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        presetSwatches.forEach { swatch ->
                            val isSelected = selectedHex.equals(swatch.hex, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(swatch.color)
                                    .then(
                                        if (isSelected)
                                            Modifier.border(3.dp, Color.White, CircleShape)
                                        else
                                            Modifier.border(1.5.dp, Color(0xFF555555), CircleShape)
                                    )
                                    .clickable {
                                        selectedHex = swatch.hex
                                        hexInput = swatch.hex
                                        hexError = false
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hex input field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Live preview dot
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(previewColor)
                            .border(1.5.dp, Color(0xFF555555), CircleShape)
                    )

                    OutlinedTextField(
                        value = hexInput,
                        onValueChange = { raw ->
                            val cleaned = raw.removePrefix("#").take(6)
                            hexInput = cleaned
                            val parsed = parseHexColor(cleaned)
                            if (parsed != null) {
                                selectedHex = cleaned
                                hexError = false
                            } else {
                                hexError = cleaned.isNotEmpty()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        label = {
                            Text(
                                if (hexError) "Invalid hex colour" else "Hex colour code",
                                color = if (hexError) Color(0xFFEF5350) else Color(0xFFAAAAAA)
                            )
                        },
                        placeholder = { Text("#2E7D32", color = Color(0xFF666666)) },
                        prefix = { Text("#", color = Color(0xFFAAAAAA)) },
                        isError = hexError,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFF555555),
                            errorBorderColor = Color(0xFFEF5350),
                            cursorColor = Color(0xFF4CAF50),
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Cancel / Save buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFAAAAAA)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF555555))
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val parsed = parseHexColor(selectedHex)
                            if (parsed != null && !hexError) {
                                onSave(selectedHex)
                            }
                        },
                        enabled = !hexError && selectedHex.length == 6,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}