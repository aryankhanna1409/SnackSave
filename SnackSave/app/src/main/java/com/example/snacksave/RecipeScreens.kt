package com.example.snacksave

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Data model for a recipe
data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val prepTime: String
)

// Sample recipes list (top-level)
val sampleRecipes = listOf(
    Recipe("Vanilla Latte", listOf("Coffee Powder", "Cold milk", "Vanilla Extract"), "5 min"),
    Recipe("Chocolate Milkshake", listOf("Ice cream", "Chocolate sauce", "Milk"), "3 min"),
    Recipe("Cold Coffee", listOf("Coffee Powder", "Cold milk", "Milk"), "3 min")
)

// --- Recipe List Screen ---
@Composable
fun RecipeListScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Recipes") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(recipes) { recipe ->
                RecipeItem(recipe, onClick = { onRecipeClick(recipe) })
                Divider()
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = recipe.name, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Ingredients: ${recipe.ingredients.joinToString(", ")}",
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Prep Time: ${recipe.prepTime}", style = MaterialTheme.typography.body2)
    }
}

// --- Spending Log Screen ---
@Composable
fun SpendingLogScreen(
    spendingLogs: List<Pair<String, String>>, // Pair<Date, Amount>
    onAddLog: (String, String) -> Unit
) {
    var dateInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Spending Logs") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(spendingLogs) { log ->
                        Text(
                            text = "${log.first}: ₹${log.second}",
                            style = MaterialTheme.typography.body1
                        )
                        Divider()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = dateInput,
                    onValueChange = { dateInput = it },
                    label = { Text("Date (DD-MM-YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Amount (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (dateInput.isNotBlank() && amountInput.isNotBlank()) {
                            onAddLog(dateInput, amountInput)
                            dateInput = ""
                            amountInput = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Add Log")
                }
            }
        }
    )
}
