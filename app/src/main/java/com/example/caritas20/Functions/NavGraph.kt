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


@Composable
fun Nav(){
    val NavController = rememberNavController()
    NavHost(navController = NavController, startDestination = "Home"){
        composable(route = "Home") {
            HomeScreen(navController = NavController)
        }
        composable(route = "Galery") {
            GaleryScreen(NavController)
        }
        composable(route = "Price") {
            PriceScreen(NavController)
        }
        composable (route = "Order") {
            OrderScreen(NavController)
        }
        composable(route  ="Add"){
            AddScreen(NavController)
        }
        composable(route = "Modify"){
            ModifyScreen(NavController)
        }
    }
}