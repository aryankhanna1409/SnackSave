// screens/RecipeListScreen.kt
package com.example.snacksave.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.snacksave.data.Recipe

@Composable
fun RecipeListScreen() {
    val recipes = remember {
        listOf(
            Recipe(
                name = "Vanilla Latte",
                ingredients = listOf("1 cup milk", "1 shot espresso", "1 tsp vanilla extract"),
                instructions = "Heat milk, add espresso and vanilla, stir well."
            ),
            Recipe(
                name = "Cold Coffee",
                ingredients = listOf("1 cup cold milk", "2 tsp instant coffee", "Ice cubes", "Sugar to taste"),
                instructions = "Blend all ingredients and serve chilled."
            ),
            Recipe(
                name = "Chocolate Milkshake",
                ingredients = listOf("1 cup milk", "2 tbsp cocoa powder", "2 tbsp sugar", "Ice cream (optional)"),
                instructions = "Blend everything till smooth and serve cold."
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Home Recipe Alternatives", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recipes) { recipe ->
                Card(
                    elevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(recipe.name, style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ingredients:", style = MaterialTheme.typography.subtitle2)
                        recipe.ingredients.forEach {
                            Text("- $it", style = MaterialTheme.typography.body2)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Instructions:", style = MaterialTheme.typography.subtitle2)
                        Text(recipe.instructions, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}
