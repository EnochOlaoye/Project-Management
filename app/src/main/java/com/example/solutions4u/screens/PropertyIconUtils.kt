package com.example.solutions4u.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class PropertyIcon(val key: String, val label: String, val icon: ImageVector)

val propertyIcons = listOf(
    PropertyIcon("home",     "Home",     Icons.Default.Home),
    PropertyIcon("person",   "Person",   Icons.Default.Person),
    PropertyIcon("location", "Location", Icons.Default.LocationOn),
    PropertyIcon("star",     "Favourite",Icons.Default.Star),
    PropertyIcon("settings", "Settings", Icons.Default.Settings),
    PropertyIcon("search",   "Search",   Icons.Default.Search),
    PropertyIcon("add",      "Add",      Icons.Default.Add),
    PropertyIcon("menu",     "Menu",     Icons.Default.Menu)
)

fun getPropertyIcon(key: String): ImageVector {
    return propertyIcons.find { it.key == key }?.icon ?: Icons.Default.Home
}