// components/PushbackDialog.kt
package com.example.snacksave.components

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable

@Composable
fun PushbackDialog(
    cafeName: String,
    onDismiss: () -> Unit,
    onReject: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Do you really need that coffee?") },
        text = { Text("You're near $cafeName. Maybe try a home recipe instead and save that ₹200.") },
        confirmButton = {
            TextButton(onClick = onReject) {
                Text("Open Café Info Anyway")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Show me an alternative")
            }
        }
    )
}
