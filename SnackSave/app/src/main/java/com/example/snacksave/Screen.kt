// navigation/Screen.kt
package com.example.snacksave.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Recipes : Screen("recipes")
    object SpendingLog : Screen("spending_log")
}
