package com.hung.currency.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hung.currencies.presentation.CurrencyListScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "currencyList") {
        composable("currencyList") {
            CurrencyListScreen()
        }
    }
}