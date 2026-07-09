// screens/SpendingLogScreen.kt
package com.example.snacksave.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.snacksave.data.SpendingEntry
import com.example.snacksave.utils.StorageUtils

@Composable
fun SpendingLogScreen(context: Context) {
    var entries by remember { mutableStateOf<List<SpendingEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        entries = StorageUtils.loadSpendingLog(context)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Spending Log", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(12.dp))

        if (entries.isEmpty()) {
            Text("No entries yet.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(entries) { entry ->
                    Card(
                        elevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Café: ${entry.cafeName}", style = MaterialTheme.typography.subtitle1)
                            Text("Amount: ₹${entry.amount}", style = MaterialTheme.typography.body1)
                            Text("Date: ${entry.timestamp}", style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }
        }
    }
}
