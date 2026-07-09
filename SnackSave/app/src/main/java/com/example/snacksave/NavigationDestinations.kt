package com.example.snacksave

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Recipes : Screen("recipes")
    object SpendingLog : Screen("spending_log")
}

