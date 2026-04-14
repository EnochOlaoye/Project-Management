package com.example.solutions4u.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solutions4u.ui.theme.*

@Composable
fun PropertyIconPickerDialog(
    currentIcon: String,
    onDismiss: () -> Unit,
    onIconSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf(currentIcon) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Property Icon", fontWeight = FontWeight.Bold) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(220.dp)
            ) {
                items(propertyIcons) { propIcon ->
                    val isSelected = selected == propIcon.key
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Green600 else LightGray)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) Green600 else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selected = propIcon.key }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = propIcon.icon,
                            contentDescription = propIcon.label,
                            tint = if (isSelected) White else DarkGray,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = propIcon.label,
                            fontSize = 10.sp,
                            color = if (isSelected) White else DarkGray
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { android.util.Log.d("ICON_UPDATE", "Select pressed, selected: $selected")
                            onIconSelected(selected) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Select", color = White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}