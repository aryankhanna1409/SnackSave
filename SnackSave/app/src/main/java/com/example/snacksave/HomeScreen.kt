// screens/HomeScreen.kt
package com.example.snacksave.screens

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    latitude: Double,
    longitude: Double,
    onTrackCafeClick: () -> Unit,
    onNavigateToRecipes: () -> Unit,
    onNavigateToSpendingLog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SnackSave", style = MaterialTheme.typography.h5)
        Text("Location: $latitude, $longitude")

        Button(onClick = onTrackCafeClick) {
            Text("Check for Cafés Nearby")
        }

        Button(onClick = onNavigateToRecipes) {
            Text("Show Recipes")
        }

        Button(onClick = onNavigateToSpendingLog) {
            Text("View Spending Log")
        }
    }
}

