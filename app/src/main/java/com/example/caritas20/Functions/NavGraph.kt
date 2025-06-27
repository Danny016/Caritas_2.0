package com.example.caritas20.Functions

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caritas20.Screens.AddScreen
import com.example.caritas20.Screens.GaleryScreen
import com.example.caritas20.Screens.HomeScreen
import com.example.caritas20.Screens.ModifyScreen
import com.example.caritas20.Screens.OrderScreen
import com.example.caritas20.Screens.PriceScreen
import com.example.caritas20.ViewModels.ViewModelFactory


@Composable
fun Nav(viewModelFactory: ViewModelFactory){
    val NavController = rememberNavController()
    NavHost(navController = NavController, startDestination = "Home"){
        composable(route = "Home") {
            HomeScreen(navController = NavController, viewModelFactory = viewModelFactory)
        }
        composable(route = "Galery") {
            GaleryScreen(NavController, viewModelFactory = viewModelFactory)
        }
        composable(route = "Price") {
            PriceScreen(NavController, viewModelFactory = viewModelFactory)
        }
        composable (route = "Order") {
            OrderScreen(NavController, viewModelFactory = viewModelFactory)
        }
        composable(route  ="Add"){
            AddScreen(NavController, viewModelFactory = viewModelFactory)
        }
        composable(route = "Modify"){
            ModifyScreen(NavController, viewModelFactory = viewModelFactory)
        }
    }
}

